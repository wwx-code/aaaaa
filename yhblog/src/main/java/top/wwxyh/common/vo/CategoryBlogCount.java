package top.wwxyh.common.vo;

import lombok.Data;

/**
 * @Description:  分类和博客数量
 * @Author: wwx
 * @Date: 2021/4/18 1:14
 */
@Data
public class CategoryBlogCount {
    private Long id;
    private String name;//分类名
    private Integer value;//分类下博客数量
}
