package top.wwxyh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.Blog;
import top.wwxyh.entity.Comment;
import top.wwxyh.entity.User;
import top.wwxyh.service.BlogService;
import top.wwxyh.service.CommentService;
import top.wwxyh.service.UserService;
import top.wwxyh.util.JwtUtils;
import top.wwxyh.util.JwtUtils1;
import top.wwxyh.util.MailUtils;
import top.wwxyh.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @Autowired
    MailUtils mailUtils;

    @Autowired
    MailProperties mailProperties;

    private static String websiteUrl;

    @Value("${server.website.url}")
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * @Author wwx
     * @Description  根据页面分页查询评论列表
     * @Date 2021/3/29 17:19
     * @Param [blogId, pageNum, pageSize, request]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/comments")
    public Result comments(@RequestParam(defaultValue = "") Long blogId,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize,
                           HttpServletRequest request){
        int judgeResult = judgeCommentEnable(blogId);
        if (judgeResult == 1){
            return Result.fail("评论已关闭");
        }else if (judgeResult == 2){
            return Result.fail("该博客不存在");
        }
        //该博客评论条数
        int count = commentService.count(new QueryWrapper<Comment>().eq("blog_id", blogId));
        Page page = new Page(pageNum,pageSize);
        IPage pageInfo = commentService.getCommentPageList(page, blogId, -1l);
        Map<String, Object> map = new HashMap<>();
        map.put("count",count);
        map.put("comment",pageInfo);
        return Result.succ(map);
    }

    /**
     * @Author wwx
     * @Description  判断对应页面评论是否开启
     * @Date 2021/3/29 11:27
     * @Param [blogId]
     * @return 0:公开可查询状态 1:评论关闭 2:该博客不存在 3:文章受密码保护
     **/
    private int judgeCommentEnable(Long blogId){
        Blog blog = blogService.getById(blogId);
        Boolean isCommentEnabled = blog.getIsCommentEnabled();
        Boolean isPublished = blog.getIsPublished();
        if (isCommentEnabled == null || isPublished == null){
            //未查询到该博客
            return 2;
        }else if (!isPublished){
            //未公开
            return 2;
        }else if (!isCommentEnabled){
            //博客评论已关闭
            return 1;
        }
        //判断文章是否存在访问密码
//        String password = blog.getPassword();
//        if (StringUtils.isEmpty(password)){
//            return 3;
//        }
        return 0;
    }

    /**
     * @Author wwx
     * @Description  保存评论
     * @Date 2021/4/15 20:13
     * @Param [comment, request]
     * @return top.wwxyh.common.lang.Result
     **/
    @PostMapping("/comment")
    public Result saveComment(@RequestBody Comment comment, HttpServletRequest request){
        //访客的评论标识
        boolean isVisitorComment;

        //判断博客页面评论权限是否开启
        int judgeResult = judgeCommentEnable(comment.getBlogId());
        if (judgeResult == 1){
            return Result.fail("评论已关闭");
        }else if (judgeResult == 2){
            return Result.fail("该博客不存在");
        }

        String jwt = request.getHeader("Authorization");
        //判断是否博主评论，博主登录会携带jwt
        if (JwtUtils.judgeTokenIsExist(jwt)){
            String subject;
            try {
                subject = JwtUtils.getTokenBody(jwt).getSubject();
            }catch (Exception e) {
                return Result.fail("Token已失效，请重新验证密码");
            }
            //获取用户名
            User admin = userService.getOne(new QueryWrapper<User>().eq("username", subject));
            //设置博主评论属性
            setAdminComment(comment,request,admin);
            isVisitorComment = false;
        }else{
            //设置访客评论属性
            setVisitorComment(comment,request);
            isVisitorComment = true;
        }

        //保存评论
        commentService.save(comment);
        //邮件提醒
        String path = "/blog/" + comment.getBlogId();
        judgeSendMail(comment, isVisitorComment, path);

        return Result.succ("评论成功");
    }

    //设置博主评论属性
    private void setAdminComment(Comment comment,  HttpServletRequest request, User admin) {
        comment.setIsAdminComment(true);
        comment.setCreateTime(LocalDateTime.now());
        comment.setIsPublished(true);
        comment.setAvatar(admin.getAvatar());
        comment.setWebsite("/");
        comment.setNickname(admin.getNickname());
        comment.setEmail(admin.getEmail());
        comment.setIp(request.getRemoteAddr());
        comment.setIsNotice(false);
    }

    //设置访客评论属性
    private void setVisitorComment(Comment comment, HttpServletRequest request) {
        //截取中间的非空白字符,即去掉字符串两端的空白字符
        String website = comment.getWebsite().trim();
        if (!"".equals(website) && !website.startsWith("http://") && !website.startsWith("https://")) {
            website = "http://" + website;
        }
        //设置访客随机头像
        setCommentRandomAvatar(comment);
        comment.setIsAdminComment(false);
        comment.setCreateTime(LocalDateTime.now());
        comment.setIsPublished(true);//默认不需要审核
        comment.setWebsite(website);
        comment.setEmail(comment.getEmail().trim());
        comment.setIp(request.getRemoteAddr());
    }

    //设置访客随机头像
    private void setCommentRandomAvatar(Comment comment) {
        //根据评论昵称取MD5，保证每一个昵称对应一个头像
        String nickname = comment.getNickname();
        char m = nickname.charAt(nickname.length() - 1);//取MD5最后一位
        int num = m % 6 + 1;//计算对应的头像
        String avatar = "/img/comment-avatar/" + num + ".jpg";
        comment.setAvatar(avatar);
    }

    //发送邮件提醒
    private void judgeSendMail(Comment comment, boolean isVisitorComment, String path) {
        //访客的评论
        if (isVisitorComment) {
            //访客回复了一条评论
            if (comment.getParentCommentId() != -1) {
                //获取该评论的父评论信息
                Comment parentComment = commentService.getById(comment.getParentCommentId());
                //访客回复我的评论，邮件提醒我自己
                if (parentComment.getIsAdminComment()) {
                    sendMailToMe(parentComment.getEmail(), path);
                } else if (parentComment.getIsNotice()) {
                    //访客回复了一个访客，且对方接收提醒，邮件提醒对方，并提醒我有新评论
                    sendMailToParentComment(parentComment.getEmail(), path);
                    sendMailToMe(mailProperties.getUsername(), path);
                }
            } else {
                String username = mailProperties.getUsername();
                //博客页面访客的直接评论，只邮件提醒我自己
                sendMailToMe(username, path);
            }
        } else {
            //我的评论
            //我回复了一条评论
            if (comment.getParentCommentId() != -1) {
                //获取该评论的父评论信息
                Comment parentComment = commentService.getById(comment.getParentCommentId());
                //我回复访客，且对方接收提醒，邮件提醒对方
                if (!parentComment.getIsAdminComment() && parentComment.getIsNotice()) {
                    sendMailToParentComment(parentComment.getEmail(), path);
                }
            }
        }
    }

    /**
     * @Author wwx
     * @Description  发送邮件提醒对象
     * @Date 2021/4/15 20:11
     * @Param [email, path]
     * @return void
     **/
    private void sendMailToParentComment(String email, String path) {
        String url = websiteUrl + path;
        String toAccount = email;
        String subject = " yhblog 评论回复";
        String content = "<body><h2>您的评论有新回复</h2><p><a href='" + url + "'>详情请看" + url + "</a></p><p>此邮件为自动发送，如不想再收到此类消息，请回复TD</p></body>";
        mailUtils.sendHTMLMail(toAccount, subject, content);
    }

    /**
     * @Author wwx
     * @Description  发送邮件提醒我自己
     * @Date 2021/4/15 20:11
     * @Param [email, path]
     * @return void
     **/
    private void sendMailToMe(String email, String path) {
        String url = websiteUrl + path;
        String toAccount = email;
        String subject = " yhblog 新评论";
        String content = "<body><p><a href='" + url + "'>新评论" + url + "</a></p></body>";
        mailUtils.sendHTMLMail(toAccount, subject, content);
    }
}
