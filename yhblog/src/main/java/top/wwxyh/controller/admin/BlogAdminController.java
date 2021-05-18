package top.wwxyh.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.dto.BlogAdmin;
import top.wwxyh.common.dto.BlogEdit;
import top.wwxyh.common.dto.BlogVisibility;
import top.wwxyh.common.lang.Result;
import top.wwxyh.common.vo.BlogDetail;
import top.wwxyh.entity.*;
import top.wwxyh.service.*;
import top.wwxyh.util.ShiroUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 博客文章后台管理
 * @Author: wwx
 * @Date: 2021/3/22 17:30
 */
@RestController
@RequestMapping("/admin")
public class BlogAdminController {

    @Autowired
    BlogService blogService;
    
    @Autowired
    CategoryService categoryService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    CommentService commentService;

    @Autowired
    TagService tagService;

    /**
     * @Author wwx
     * @Description  获取所有博客文章列表
     * @Date 2021/3/23 11:16
     * @Param [title, categoryId, pageNum, pageSize]
     * @Param [按标题模糊查询, 按分类id查询, 当前页数, 每页个数]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/blogs")
    public Result blogs(@RequestParam(defaultValue = "") String title,
                        @RequestParam(defaultValue = "") Integer categoryId,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize){
        //设置分页对象
        Page page = new Page(pageNum, pageSize);
        Page pageInfo = blogService.getListByTitleAndCategoryId(page, title, categoryId);

        //获取所有分类List
        List<Category> categories = categoryService.list(new QueryWrapper<Category>().orderByDesc("id"));

        HashMap<String, Object> map = new HashMap<>();
        map.put("blogs", pageInfo);
        map.put("categories", categories);
        return Result.succ(map);
    }

    /**
     * @Author wwx
     * @Description  更新博客置顶状态
     * @Date 2021/3/23 11:42
     * @Param [id, isTop]
     * @Param [博客id, 是否置顶]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新博客置顶状态")
    @GetMapping("/blog/top")
    public Result updateTop(@RequestParam Long id,@RequestParam boolean isTop){
        blogService.update(new UpdateWrapper<Blog>().eq("id", id).set("is_top", isTop));
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  更新博客推荐状态
     * @Date 2021/3/23 14:16
     * @Param [id, isRecommend]
     * @Param [博客id, 是否推荐]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新博客推荐状态")
    @GetMapping("/blog/recommend")
    public Result updateRecommend(@RequestParam Long id,@RequestParam boolean isRecommend){
        blogService.update(new UpdateWrapper<Blog>().eq("id",id).set("is_recommend",isRecommend));
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  更新博客可见性状态
     * @Date 2021/3/23 15:31
     * @Param [id, blogVisibility]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新博客可见性状态")
    @PostMapping("/blog/{id}/visibility")
    public Result updateVisibility(@PathVariable Long id, @RequestBody BlogVisibility blogVisibility){
        UpdateWrapper<Blog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("is_published",blogVisibility.getIsPublished())
                .set("password",blogVisibility.getPassword())
                .set("is_appreciation",blogVisibility.getIsAppreciation())
                .set("is_recommend",blogVisibility.getIsRecommend())
                .set("is_comment_enabled",blogVisibility.getIsCommentEnabled())
                .set("is_top",blogVisibility.getIsTop());

        blogService.update(updateWrapper);
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  根据id删除博客文章
     * @Date 2021/3/23 17:39
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("删除博客")
    @GetMapping("/blog/delete")
    public Result deleteBlogById(@RequestParam Long id){
        //1、根据博客id删除blog_tag表里绑定的博客标签数据
        blogTagService.remove(new QueryWrapper<BlogTag>().eq("blog_id",id));

        //2、根据id删除博客
        blogService.removeById(id);

        //3、删除博客文章下的所有评论
        commentService.remove(new QueryWrapper<Comment>().eq("blog_id",id));

        return Result.succ("删除博客成功");
    }

    /**
     * @Author wwx
     * @Description  根据id获取博客详情
     * @Date 2021/3/23 17:47
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/blog")
    public Result getBlogById(@RequestParam Long id){
        BlogAdmin blog = blogService.getBlogById(id);
        return Result.succ(blog);
    }

    /**
     * @Author wwx
     * @Description  获取分类列表和标签列表
     * @Date 2021/3/23 20:26
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/categoryAndTag")
    public Result categoryAndTag(){
        List<Category> categories = categoryService.list();
        List<Tag> tags = tagService.list();
        HashMap<String, Object> map = new HashMap<>();
        map.put("categories",categories);
        map.put("tags",tags);
        return Result.succ(map);
    }

    /**
     * @Author wwx
     * @Description  新增博客
     * @Date 2021/3/23 22:04
     * @Param [blogEdit]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("发布博客")
    @PostMapping("/blog/add")
    public Result saveBlog(@Validated @RequestBody BlogEdit blogEdit){
        Blog temp = new Blog();

        temp.setUserId((long) 1);
        //temp.setUserId(ShiroUtils.getProfile().getId());
        temp.setCreateTime(LocalDateTime.now());

        BeanUtil.copyProperties(blogEdit,temp,"id","userId","createTime");
        blogService.save(temp);

        //关联博客和标签(维护 blog_tag 表)
        List<Long> tagList = blogEdit.getTagList();
        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(temp.getId());
        for (Long tag : tagList){
            blogTag.setTagId(tag);
            blogTagService.save(blogTag);
        }

        return Result.succ("新增博客成功");
    }

    /**
     * @Author wwx
     * @Description  更新博客
     * @Date 2021/3/23 23:11
     * @Param [blogEdit]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新博客")
    @PostMapping("/blog/update")
    public Result updateBlog(@Validated @RequestBody BlogEdit blogEdit){
        Blog temp = new Blog();

        temp.setUpdateTime(LocalDateTime.now());

        BeanUtil.copyProperties(blogEdit,temp,"updateTime");
        blogService.saveOrUpdate(temp);

        //关联博客和标签(维护 blog_tag 表)
        //根据博客id删除blog_tag表里绑定的博客标签数据
        blogTagService.remove(new QueryWrapper<BlogTag>().eq("blog_id",temp.getId()));
        List<Long> tagList = blogEdit.getTagList();
        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(temp.getId());
        for (Long tag : tagList){
            blogTag.setTagId(tag);
            blogTagService.save(blogTag);
        }

        return Result.succ("更新博客成功");
    }
}
