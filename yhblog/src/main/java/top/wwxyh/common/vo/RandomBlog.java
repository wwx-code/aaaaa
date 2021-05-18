package top.wwxyh.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 随机博客
 * @Author: wwx
 * @Date: 2021/4/12 10:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomBlog {
    private Long id;
    private String title;//文章标题

    private Category category;//文章分类
    private List<Tag> tags = new ArrayList<>();//文章标签
}
