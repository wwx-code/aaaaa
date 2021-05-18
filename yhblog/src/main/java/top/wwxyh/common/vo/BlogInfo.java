package top.wwxyh.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:  博客简要信息
 * @Author: wwx
 * @Date: 2021/3/27 22:25
 */
@Data
public class BlogInfo {
    private Long id;
    private String title;//文章标题
    private String description;//描述
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    private Integer views;//浏览次数
    private Integer words;//文章字数
    private Integer readTime;//阅读时长(分钟)
    private Boolean isTop;//是否置顶
    private String password;//文章密码
    private Boolean privacy;//是否私密文章

    private Category category;//文章分类
    private List<Tag> tags = new ArrayList<>();//文章标签
}
