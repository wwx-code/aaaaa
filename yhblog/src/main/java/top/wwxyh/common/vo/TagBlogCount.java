package top.wwxyh.common.vo;

import lombok.Data;

/**
 * @Description:  标签和博客数量
 * @Author: wwx
 * @Date: 2021/4/18 2:14
 */
@Data
public class TagBlogCount {
    private Long id;
    private String name;//标签名
    private Integer value;//标签下博客数量
}
