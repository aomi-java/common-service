package tech.aomi.common.data;


import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

/**
 * @author Sean createAt 2021/6/9
 */
public class PageUtil {

    public static <T> PageResult<T> toPage(Page<T> page) {
        PageResult<T> result = new PageResult<>();

        if (CollectionUtils.isEmpty(page.getContent())) {
            result.setContent(Collections.emptyList());
        } else {
            result.setContent(page.getContent());
        }
        result.setFirst(page.isFirst());
        result.setLast(page.isLast());
        result.setNumber(page.getNumber());
        result.setNumberOfElements(page.getNumberOfElements());
        result.setSize(page.getSize());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        return result;
    }

}
