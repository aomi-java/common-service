package tech.aomi.common.service;

import tech.aomi.common.exception.ServiceException;

import java.io.Serializable;
import java.util.Optional;

/**
 * CRUD 服务接口
 *
 * @author Sean createAt 2022/5/9
 */
public interface CrudService<T> {

    /**
     * 根据ID查询
     *
     * @param id 对象ID
     * @return 查询结果
     */
    default Optional<T> findById(Serializable id) {
        throw new ServiceException("服务未实现");
    }

    default boolean existById(Serializable id) {
        throw new ServiceException("服务未实现");
    }

    /**
     * 创建
     *
     * @param entity 数据对象
     * @return 结果
     */
    default T create(T entity) {
        throw new ServiceException("服务未实现");
    }

    default T update(Serializable id, T entity) {
        throw new ServiceException("服务未实现");
    }

    default T update(T entity) {
        throw new ServiceException("服务未实现");
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据对象
     * @return 数据对象
     */
    default T persist(T entity) {
        throw new ServiceException("服务未实现");
    }

    default int deleteById(Serializable id) {
        throw new ServiceException("服务未实现");
    }

    default void delete(T entity) {
        throw new ServiceException("服务未实现");
    }
}
