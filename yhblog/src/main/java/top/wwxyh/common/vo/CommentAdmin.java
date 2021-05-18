package top.wwxyh.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 评论后台管理评论列表
 * @Author: wwx
 * @Date: 2021/3/26 9:52
 */
@Data
public class CommentAdmin {
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String ip;
    private boolean isPublished;
    private boolean isAdminComment;
    private boolean isNotice;
    private Long parentCommentId;
    private String website;
    private String qq;

    private BlogIdAndTitle blog;
    private List<CommentAdmin> replyComments = new ArrayList<>();
}
