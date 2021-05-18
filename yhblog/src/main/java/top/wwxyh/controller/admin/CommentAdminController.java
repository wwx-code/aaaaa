package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.common.vo.BlogIdAndTitle;
import top.wwxyh.common.vo.CommentAdmin;
import top.wwxyh.entity.Blog;
import top.wwxyh.entity.Comment;
import top.wwxyh.service.BlogService;
import top.wwxyh.service.CommentService;

import java.util.List;

/**
 * @Description:  博客评论后台管理
 * @Author: wwx
 * @Date: 2021/3/24 15:24
 */
@RestController
@RequestMapping("/admin")
public class CommentAdminController {

    @Autowired
    CommentService commentService;

    @Autowired
    BlogService blogService;

    /**
     * @Author wwx
     * @Description  按博客id分页查询评论List
     * @Date 2021/3/26 14:36
     * @Param [pageNum, pageSize, blogId]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/comments")
    public Result commments(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") Long blogId){
        Page page = new Page(pageNum,pageSize);
        Page pageInfo = commentService.getListByParentCommentId(page, blogId, -1l);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  后台管理评论页面查询博客id和标题
     * @Date 2021/4/3 17:56
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/getBlogIdAndTitle")
    public Result getBlogIdAndTitle(){
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","title")
                .orderByDesc("create_time");
        List<Blog> blogList = blogService.list(queryWrapper);
        return Result.succ(blogList);
    }

    /**
     * @Author wwx
     * @Description  更新评论公开状态
     * @Date 2021/3/26 14:52
     * @Param [id, isPublish]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新评论公开状态")
    @GetMapping("/comment/published")
    public Result updatePublished(@RequestParam Long id,@RequestParam boolean isPublished){
        commentService.update(new UpdateWrapper<Comment>().eq("id", id).set("is_published", isPublished));
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  更新接收邮件提醒状态
     * @Date 2021/3/26 14:55
     * @Param [id, isNotice]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新评论邮件提醒状态")
    @GetMapping("/comment/notice")
    public Result updateNotice(@RequestParam Long id, @RequestParam boolean isNotice){
        commentService.update(new UpdateWrapper<Comment>().eq("id",id).set("is_notice",isNotice));
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  根据id删除评论及其所有子评论
     * @Date 2021/3/26 14:57
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("删除评论")
    @GetMapping("/comment/delete")
    public Result deleteById(@RequestParam Long id){
        commentService.removeById(id);
//        commentService.deleteCommentById(id);
        return Result.succ("删除评论成功");
    }

    /**
     * @Author wwx
     * @Description  管理员更新评论
     * @Date 2021/3/26 15:05
     * @Param [comment]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新评论")
    @PostMapping("/comment/update")
    public Result updateComment(@RequestBody Comment comment){
        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",comment.getId())
                .set("nickname",comment.getNickname())
                .set("avatar",comment.getAvatar())
                .set("email",comment.getEmail())
                .set("website",comment.getWebsite())
                .set("ip",comment.getIp())
                .set("content",comment.getContent());

        commentService.update(updateWrapper);
        return Result.succ("更新评论成功");
    }
}
