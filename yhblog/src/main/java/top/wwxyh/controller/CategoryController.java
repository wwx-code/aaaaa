package top.wwxyh.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.service.BlogService;
import top.wwxyh.service.CategoryService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    BlogService blogService;

    /**
     * @Author wwx
     * @Description  根据分类name分页查询公开博客列表，sql查询语句有bug，已解决，URL拼接时不带''就行
     * 直接categoryName=分类名
     * @Date 2021/3/28 14:14
     * @Param [pageNum, categoryName]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "查看分类")
    @GetMapping("/category")
    public Result category1(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam String categoryName){
        Page page = new Page(pageNum, 5);
        IPage pageInfo = blogService.getBlogInfoListByCategoryNameAndIsPublished(page, categoryName);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据分类id分页查询公开博客列表
     * @Date 2021/3/28 14:13
     * @Param [pageNum, categoryId]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/categoryId")
    public Result category(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam Integer categoryId){
        Page page = new Page(pageNum, 5);
        IPage pageInfo = blogService.getBlogInfoListByCategoryIdAndIsPublished(page, categoryId);
        return Result.succ(pageInfo);
    }
}
