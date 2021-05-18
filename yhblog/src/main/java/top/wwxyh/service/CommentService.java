package top.wwxyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.wwxyh.common.vo.CommentAdmin;
import top.wwxyh.common.vo.CommentPage;
import top.wwxyh.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
public interface CommentService extends IService<Comment> {
    Page<CommentAdmin> getListByParentCommentId(Page<CommentAdmin> page, Long blogId, Long parentCommentId);

    void deleteCommentById(Long commentId);

    IPage<CommentPage> getCommentPageList(Page<CommentPage> page, Long blogId, Long parentCommentId);
}
