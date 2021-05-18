package top.wwxyh.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.Tag;
import top.wwxyh.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 后台博客管理博客展示列表
 * @Author: wwx
 * @Date: 2021/3/26 14:28
 */
@Data
public class BlogAdmin {
    private Long id;
    private String title;//文章标题
    private String content;//文章正文
    private String description;//描述
    private Boolean isPublished;//公开或私密
    private Boolean isRecommend;//推荐开关
    private Boolean isAppreciation;//赞赏开关
    private Boolean isCommentEnabled;//评论开关
    private Boolean isTop;//是否置顶
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
    private Integer views;//浏览次数
    private Integer words;//文章字数
    private Integer readTime;//阅读时长(分钟)
    private String password;//密码保护

    private User user;//文章作者(因为是个人博客，也可以不加作者字段，暂且加上)
    private Category category;//文章分类
    private List<Tag> tags = new ArrayList<>();//文章标签
}
