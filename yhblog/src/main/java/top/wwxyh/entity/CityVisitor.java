package top.wwxyh.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("city_visitor")
public class CityVisitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 独立访客数量
     */
    private Integer uv;


}
