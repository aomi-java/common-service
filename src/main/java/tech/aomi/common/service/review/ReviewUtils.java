package tech.aomi.common.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tech.aomi.common.entity.review.*;
import tech.aomi.common.entity.system.Operator;
import tech.aomi.common.exception.AccessDeniedException;
import tech.aomi.common.exception.CustomErrorMessageException;
import tech.aomi.common.exception.ResourceNonExistException;
import tech.aomi.common.exception.ResourceStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ReviewUtils {

    public static final String[] IGNORE_PROPERTIES = new String[]{"id", "lastReviewId", "reviewStatus"};

    /**
     * 创建审核记录
     *
     * @param review        审核记录实例
     * @param reviewProcess 审核流程
     * @param operator      操作员信息
     * @param describe      变更说明
     * @param <T>           审核实例
     * @return 审核实例记录
     */
    public static <R, T extends Review<R>> T create(T review, R before, R after, ReviewProcess reviewProcess, Operator<?> operator, String describe) {
        if (null == reviewProcess) {
            throw new ResourceNonExistException("未配置审核流程");
        } else if (null == reviewProcess.getChain()) {
            throw new ResourceNonExistException("当前审核链中未配置审核用户");
        }

        review.setReviewProcess(reviewProcess);
        review.setCurrentReviewUserIndex(0);
        review.setNextReviewUserIndex(1);
        review.setStatus(ReviewStatus.WAIT);

        List<ReviewHistory> histories = new ArrayList<>();
        ReviewHistory reviewHistory = new ReviewHistory();
        reviewHistory.setDescribe(describe);
        reviewHistory.setUser(operator);
        reviewHistory.setReviewAt(new Date());
        reviewHistory.setResult(ReviewResult.RESOLVE);
        histories.add(reviewHistory);
        review.setHistories(histories);

        review.setDescribe(describe);
        review.setCreateAt(new Date());
        review.setBefore(before);
        review.setAfter(after);

        return review;
    }

    /**
     * 审核
     *
     * @param review         待审核记录
     * @param operator       操作员信息
     * @param result         审核结果
     * @param resultDescribe 审核结果说明
     * @param <T>            具体审核实例
     * @return 审核实例
     */
    public static <T extends Review<?>> T review(T review, Operator<?> operator, ReviewResult result, String resultDescribe) {
        if (review.getStatus() == ReviewStatus.FINISH) {
            LOGGER.error("审核流程已经结束: {}", review.getStatus());
            throw new ResourceStatusException("审核流程已结束");
        }

        List<ReviewUser> reviewUsers = review.getReviewProcess().getChain();
        int currentReviewIndex = review.getCurrentReviewUserIndex();
        LOGGER.debug("当前审核组下标: {}", currentReviewIndex);
        if (currentReviewIndex < 0 || currentReviewIndex > reviewUsers.size()) {
            LOGGER.error("审核流程无法获取当前审核的用户信息");
            throw new CustomErrorMessageException("96", "审核流程无法获取当前审核的用户信息");
        }
        ReviewUser reviewUser = reviewUsers.get(currentReviewIndex);

        AtomicReference<String> roleId = new AtomicReference<>("");

        boolean canReview;
        if (StringUtils.hasLength(reviewUser.getUserId()) && reviewUser.getUserId().equals(operator.getId())) {
            canReview = true;
        } else {
            canReview = operator.getRoles().stream().anyMatch(role -> {
                //指定用户审核时非法用户提交会因为缺失权限判断时会出现空指针
                if (ObjectUtils.isEmpty(reviewUser.getRoleId())) {
                    return false;
                }
                boolean tmp = reviewUser.getRoleId().equals(role.getId());
                if (tmp) {
                    roleId.set(role.getId());
                }
                return tmp;
            });
        }
        if (!canReview) {
            LOGGER.error("用户不能进行该审核操作");
            throw new AccessDeniedException("用户不能进行该审核操作");
        }

        //add history
        if (null == review.getHistories()) {
            review.setHistories(new ArrayList<>());
        }
        ReviewHistory history = new ReviewHistory();
        history.setUser(operator);
        history.setResult(result);
        history.setDescribe(resultDescribe);
        history.setReviewAt(new Date());
        review.getHistories().add(history);

        //审核结果
        if (ReviewResult.RESOLVE == result) {
            ReviewProcess process = review.getReviewProcess();
            if (review.getNextReviewUserIndex() == process.getChain().size()) {
                // 流程结束
                review.setStatus(ReviewStatus.FINISH);
                review.setResult(ReviewResult.RESOLVE);
                review.setResultDescribe(resultDescribe);
                review.setNextReviewUserIndex(-1);
            } else {
                // 移交下一步
                review.setStatus(ReviewStatus.PROCESSING);
                review.setCurrentReviewUserIndex(review.getCurrentReviewUserIndex() + 1);
                review.setNextReviewUserIndex(review.getNextReviewUserIndex() + 1);
            }
            return review;
        }
        //驳回审核
        review.setStatus(ReviewStatus.FINISH);
        review.setResult(result);
        review.setResultDescribe(resultDescribe);
        return review;
    }

    public static void copy(Object source, Object target, String... ignoreProperties) {
        String[] s;
        if (null == ignoreProperties) {
            s = IGNORE_PROPERTIES;
        } else {
            s = new String[ignoreProperties.length + IGNORE_PROPERTIES.length];
            System.arraycopy(ignoreProperties, 0, s, 0, ignoreProperties.length);
            System.arraycopy(IGNORE_PROPERTIES, 0, s, ignoreProperties.length, IGNORE_PROPERTIES.length);
        }
        BeanUtils.copyProperties(source, target, s);
    }
}
