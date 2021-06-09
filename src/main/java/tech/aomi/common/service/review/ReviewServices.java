package tech.aomi.common.service.review;


import tech.aomi.common.entity.review.Review;
import tech.aomi.common.entity.review.ReviewResult;
import tech.aomi.common.entity.system.Operator;

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
     * @param operator       审核操作员
     * @param result         审核结果
     * @param resultDescribe 审核结果说明
     */
    <T extends Review> T review(Class<T> clazz, String reviewId, Operator<?> operator, ReviewResult result, String resultDescribe);

}
