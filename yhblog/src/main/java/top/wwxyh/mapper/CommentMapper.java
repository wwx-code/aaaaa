package top.wwxyh.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;
import top.wwxyh.common.vo.CommentAdmin;
import top.wwxyh.common.vo.CommentPage;
import top.wwxyh.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentAdmin> getListByParentCommentId(Page<CommentAdmin> page, Long blogId, Long parentCommentId);

    List<CommentAdmin> getListByParentCommentId(Long parentCommentId);

    List<CommentPage> getCommentPageListByParentCommentId(Page<CommentPage> page, Long blogId, Long parentCommentId);
}
