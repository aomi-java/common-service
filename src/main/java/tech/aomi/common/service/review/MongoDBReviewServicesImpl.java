package tech.aomi.common.service.review;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;
import tech.aomi.common.entity.review.Review;
import tech.aomi.common.entity.review.ReviewResult;
import tech.aomi.common.entity.system.Operator;
import tech.aomi.common.exception.ResourceNonExistException;


/**
 * 基于mongodb实现的审核服务
 *
 * @author Sean(sean.snow @ live.com) Create At 2019-11-19
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MongoDBReviewServicesImpl implements ReviewServices {

    private MongoTemplate mongoTemplate;

    @Override
    public <T extends Review> T review(Class<T> clazz, String reviewId, Operator<?> operator, ReviewResult result, String resultDescribe) {
        Assert.hasLength(reviewId, "审核流程ID不能为空");
        T review = mongoTemplate.findById(reviewId, clazz);
        if (null == review) {
            LOGGER.error("审核流程不存在: {}, {}", clazz.getName(), reviewId);
            throw new ResourceNonExistException("审核流程不存在" + reviewId);
        }
        ReviewUtils.review(review, operator, result, resultDescribe);
        return mongoTemplate.save(review);
    }

}
