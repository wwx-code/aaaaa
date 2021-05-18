package top.wwxyh.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 页面评论
 * @Author: wwx
 * @Date: 2021/3/29 16:49
 */
@Data
public class CommentPage {
    private Long id;
    private String nickname;//昵称
    private String content;//评论内容
    private String avatar;//头像(图片路径)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//评论时间
    private String website;//个人网站
    private Boolean adminComment;//博主回复
    private String parentCommentId;//父评论id
    private String parentCommentNickname;//父评论昵称

    private List<CommentPage> replyComments = new ArrayList<>();//回复该评论的评论
}
