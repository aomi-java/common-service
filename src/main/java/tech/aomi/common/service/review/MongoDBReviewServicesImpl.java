package tech.aomi.common.service.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;
import tech.aomi.common.entity.review.Review;
import tech.aomi.common.entity.review.ReviewResult;
import tech.aomi.common.entity.review.ReviewUser;
import tech.aomi.common.exception.ResourceNonExistException;


/**
 * 基于mongodb实现的审核服务
 *
 * @author Sean(sean.snow @ live.com) Create At 2019-11-19
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MongoDBReviewServicesImpl implements ReviewServices {

    private MongoTemplate mongoTemplate;

    @Override
    public <T extends Review<R>, R> T review(Class<T> clazz, String reviewId, ReviewUser reviewUser, ReviewResult result, String resultDescribe) {
        Assert.hasLength(reviewId, "审核流程ID不能为空");
        T review = mongoTemplate.findById(reviewId, clazz);
        if (null == review) {
            LOGGER.error("审核流程不存在: {}, {}", clazz.getName(), reviewId);
            throw new ResourceNonExistException("审核流程不存在" + reviewId);
        }
        ReviewUtils.review(review, reviewUser, result, resultDescribe);
        return mongoTemplate.save(review);
    }
}
