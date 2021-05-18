package top.wwxyh.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 博客可见性dto
 * @Author: wwx
 * @Date: 2021/3/23 14:55
 */
@Data
//@NoArgsConstructor 生成无参构造
public class BlogVisibility {

    private Boolean isPublished;
    private String password;

    private Boolean isAppreciation;
    private Boolean isRecommend;
    private Boolean isCommentEnabled;
    private Boolean isTop;
}
