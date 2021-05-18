package top.wwxyh.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @Description: 博客编辑dto
 * @Author: wwx
 * @Date: 2021/3/23 21:12
 */
@Data
public class BlogEdit {
    private Long id;
    private String title;//文章标题
    private String content;//文章正文
    private String description;//描述
    private Boolean isPublished;//公开或私密
    private Boolean isRecommend;//推荐开关
    private Boolean isAppreciation;//赞赏开关
    private Boolean isCommentEnabled;//评论开关
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
    private Integer views;//浏览次数
    private Integer words;//文章字数
    private Integer readTime;//阅读时长(分钟)
    private Long categoryId;//文章分类
    private Boolean isTop;//是否置顶
    private String password;//密码保护

    private User user;//文章作者
    private List<Long> tagList;//

}
