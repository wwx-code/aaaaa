package top.wwxyh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.common.vo.CommentAdmin;
import top.wwxyh.common.vo.CommentPage;
import top.wwxyh.entity.Comment;
import top.wwxyh.mapper.CommentMapper;
import top.wwxyh.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    /**
     * @Author wwx
     * @Description  后台评论管理页面查询评论
     * @Date 2021/3/29 17:05
     * @Param [page, blogId, parentCommentId]
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<top.wwxyh.common.vo.CommentAdmin>
     **/
    @Override
    public Page<CommentAdmin> getListByParentCommentId(Page<CommentAdmin> page, Long blogId, Long parentCommentId) {
        List<CommentAdmin> comments = commentMapper.getListByParentCommentId(page, blogId, parentCommentId);
        //后边的递归调用getListByParentCommentId会把page的total和pages覆盖掉，此处先取出结果，循环后赋值进去
        long total = page.getTotal();
        long pages = page.getPages();
        for (CommentAdmin c : comments) {
            List<CommentAdmin> replayComment = commentMapper.getListByParentCommentId(page, blogId, c.getId());
            c.setReplyComments(replayComment);
        }
        page.setTotal(total);
        page.setPages(pages);
        return page.setRecords(comments);
    }

    @Override
    public void deleteCommentById(Long commentId){
        List<CommentAdmin> comments = getAllReplyComments(commentId);
        for (CommentAdmin comment : comments) {
            delete(comment);
        }
    }

    /**
     * @Author wwx
     * @Description  递归删除子评论
     * @Date 2021/3/26 15:40
     * @Param [commentAdmin]
     * @return void
     **/
    private void delete(CommentAdmin commentAdmin) {
        for (CommentAdmin c : commentAdmin.getReplyComments()) {
            delete(c);
            commentMapper.deleteById(c.getId());
        }
    }

    /**
     * @Author wwx
     * @Description  按id递归查询子评论
     * @Date 2021/3/26 15:44
     * @Param [parentCommentId]
     * @return java.util.List<top.wwxyh.common.vo.CommentAdmin>
     **/
    private List<CommentAdmin> getAllReplyComments(Long parentCommentId) {
        List<CommentAdmin> comments = commentMapper.getListByParentCommentId(parentCommentId);
        for (CommentAdmin c : comments) {
            List<CommentAdmin> replyComments = getAllReplyComments(c.getId());
            c.setReplyComments(replyComments);
        }
        return comments;
    }

    @Override
    public IPage<CommentPage> getCommentPageList(Page<CommentPage> page, Long blogId, Long parentCommentId) {
        List<CommentPage> comments = getCommentPageListByParentCommentId(page, blogId, parentCommentId);

        for (CommentPage c : comments) {
            List<CommentPage> tmpComments = new ArrayList<>();
            getReplyComments(tmpComments, c.getReplyComments());
            c.setReplyComments(tmpComments);
        }

        return page.setRecords(comments);
    }

    /**
     * 将所有子评论递归取出到一个List中
     *
     * @param comments
     * @return
     */
    private void getReplyComments(List<CommentPage> tmpComments, List<CommentPage> comments) {
        for (CommentPage c : comments) {
            tmpComments.add(c);
            getReplyComments(tmpComments, c.getReplyComments());
        }
    }

    private List<CommentPage> getCommentPageListByParentCommentId(Page<CommentPage> page, Long blogId, Long parentCommentId) {
        List<CommentPage> comments = commentMapper.getCommentPageListByParentCommentId(page, blogId, parentCommentId);
        //后边的递归调用getCommentPageListByParentCommentId会把page的total和pages覆盖掉，此处先取出结果，循环后赋值进去
        long total = page.getTotal();
        long pages = page.getPages();
        for (CommentPage c : comments) {
            List<CommentPage> replyComments = getCommentPageListByParentCommentId(page, blogId, c.getId());
            c.setReplyComments(replyComments);
        }
        page.setTotal(total);
        page.setPages(pages);
        return comments;
    }
}
