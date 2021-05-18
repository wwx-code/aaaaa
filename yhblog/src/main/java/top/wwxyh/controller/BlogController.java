package top.wwxyh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.common.vo.BlogDetail;
import top.wwxyh.common.vo.BlogInfo;
import top.wwxyh.entity.Blog;
import top.wwxyh.service.BlogService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    /**
     * @Author wwx
     * @Description  按置顶、创建时间排序 分页查询博客简要信息列表
     * @Date 2021/3/27 23:24
     * @Param [pageNum]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "访问页面", content = "首页")
    @GetMapping("/blogs")
    public Result blogs(@RequestParam(defaultValue = "1") Integer pageNum){
        Page page = new Page(pageNum, 5);
        IPage pageInfo = blogService.getBlogInfoByIsPublish(page, pageNum);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据博客id获取博客详情
     * @Date 2021/3/28 13:06
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "查看博客")
    @GetMapping("/blog")
    public Result getBlogDetailById(@RequestParam Long id){
        BlogDetail blog = blogService.getBlogByIdAndIsPublished(id);

        //查看博客详情后浏览次数+1
        Integer views = blog.getViews();
        UpdateWrapper<Blog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",blog.getId())
                .set("views",views+1);
        blogService.update(updateWrapper);
        return Result.succ(blog);
    }

    //搜索博客
    
}
