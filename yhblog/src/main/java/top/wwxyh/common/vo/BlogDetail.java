package top.wwxyh.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:  博客详情
 * @Author: wwx
 * @Date: 2021/3/28 9:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetail {
    private Long id;
    private String title;//文章标题
    private String content;//文章正文
    private Boolean isAppreciation;//赞赏开关
    private Boolean isCommentEnabled;//评论开关
    private Boolean isTop;//是否置顶
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
    private Integer views;//浏览次数
    private Integer words;//文章字数
    private Integer readTime;//阅读时长(分钟)
    private String password;//密码保护

    private Category category;//文章分类
    private List<Tag> tags = new ArrayList<>();//文章标签
}
