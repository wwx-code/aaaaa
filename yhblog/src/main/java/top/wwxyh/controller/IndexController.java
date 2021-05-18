package top.wwxyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wwxyh.common.lang.Result;
import top.wwxyh.common.vo.BlogIdAndTitle;
import top.wwxyh.common.vo.RandomBlog;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.Tag;
import top.wwxyh.service.BlogService;
import top.wwxyh.service.CategoryService;
import top.wwxyh.service.TagService;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 首页
 * @Author: wwx
 * @Date: 2021/4/12 10:11
 */
@RestController
public class IndexController {
    @Autowired
    BlogService blogService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagService tagService;

    /**
     * @Author wwx
     * @Description  首页获取最新推荐博客、分类列表、标签云、随机博客
     * @Date 2021/4/12 10:40
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/site")
    public Result site(){
        HashMap<String, Object> map = new HashMap<>();
        List<BlogIdAndTitle> newBlogList = blogService.getNewBlogListByIsPublishedAndIsRecommend();
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().orderByDesc("id"));
        List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().orderByDesc("id"));
        List<RandomBlog> randomBlogList = blogService.getRandomBlogListByLimitNumAndIsPublished();
        map.put("newBlogList",newBlogList);
        map.put("categoryList",categoryList);
        map.put("tagList",tagList);
        map.put("randomBlogList",randomBlogList);
        return Result.succ(map);
    }
}
