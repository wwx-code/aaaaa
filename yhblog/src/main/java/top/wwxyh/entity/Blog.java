package top.wwxyh.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章正文
     */
    private String content;

    /**
     * 描述
     */
    private String description;

    /**
     * 公开或私密
     */
    private Boolean isPublished;

    /**
     * 推荐开关
     */
    private Boolean isRecommend;

    /**
     * 赞赏开关
     */
    private Boolean isAppreciation;

    /**
     * 评论开关
     */
    private Boolean isCommentEnabled;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 文章字数
     */
    private Integer words;

    /**
     * 阅读时长(分钟)
     */
    private Integer readTime;

    /**
     * 文章分类
     */
    private Long categoryId;

    /**
     * 文章作者
     */
    private Long userId;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 密码保护
     */
    private String password;

//    private User user;//文章作者(因为是个人博客，也可以不加作者字段，暂且加上)
    //private Category category;//文章分类
    //private List<Tag> tags = new ArrayList<>();//文章标签
}
