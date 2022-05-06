package tech.aomi.common.service.review;


import tech.aomi.common.entity.review.Review;
import tech.aomi.common.entity.review.ReviewResult;
import tech.aomi.common.entity.review.ReviewUser;

/**
 * 审核服务
 *
 * @author Sean(sean.snow @ live.com) Create At 2019-11-19
 */
public interface ReviewServices {

    /**
     * 审核
     *
     * @param clazz          审核信息真实对象class
     * @param reviewId       审核内容ID
     * @param reviewUser     审核用户
     * @param result         审核结果
     * @param resultDescribe 审核结果说明
     * @param <T>            审核记录类型
     * @param <R>            审核资源类型
     * @return 审核记录
     */
    <T extends Review<R>, R> T review(Class<T> clazz, String reviewId, ReviewUser reviewUser, ReviewResult result, String resultDescribe);

}
