package tech.aomi.common.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部封装的分页对象
 *
 * @author Sean createAt 2016/1/18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements java.io.Serializable {

    private static final long serialVersionUID = -5309387050012144808L;

    private List<T> content = new ArrayList<>();

    private Long totalElements;

    private Integer totalPages;

    private Integer number;

    private Integer numberOfElements;

    private Integer size;

    private Boolean first;

    private Boolean last;

    private Object value;

}
