package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.Blog;
import top.wwxyh.entity.Category;
import top.wwxyh.service.BlogService;
import top.wwxyh.service.CategoryService;
import top.wwxyh.util.StringUtils;

/**
 * @Description: 博客分类后台管理
 * @Author: wwx
 * @Date: 2021/3/21 20:27
 */
@RestController
@RequestMapping("/admin")
public class CategoryAdminController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    BlogService blogService;

    /**
     * @Author wwx
     * @Description  获取博客分类列表
     * @Date 2021/3/21 20:42
     * @Param [pageNum]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/categories")
    public Result categories(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        Page page = new Page(pageNum,pageSize);
        IPage<Category> pageInfo = categoryService.page(page, new QueryWrapper<Category>().orderByDesc("id"));
        return Result.succ("获取博客分类列表成功",pageInfo);
    }

    /**
     * @Author wwx
     * @Description  新增分类
     * @Date 2021/3/21 21:04
     * @Param [category]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("添加分类")
    @PostMapping("/saveCategory")
    public Result saveCategory(@RequestBody Category category){
        if (StringUtils.isEmpty(category.getCategoryName())){
            return Result.fail("分类名称不能为空");
        }

        //查询分类是否已存在
        Category category1 = categoryService.getOne(new QueryWrapper<Category>().eq("category_name", category.getCategoryName()));
        if (category1 != null){
            return Result.fail("该分类已存在，请勿重复添加");
        }

        categoryService.save(category);
        return Result.succ("新增分类成功");
    }

    /**
     * @Author wwx
     * @Description  更新分类
     * @Date 2021/3/21 21:32
     * @Param [category]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("修改分类")
    @PostMapping("/updateCategory")
    public Result updateCategory(@RequestBody Category category){
        if (StringUtils.isEmpty(category.getCategoryName())){
            return Result.fail("分类名称不能为空");
        }

        //查询分类是否已存在
        Category category1 = categoryService.getOne(new QueryWrapper<Category>().eq("category_name", category.getCategoryName()));
        if (category1 != null){
            return Result.fail("更新的分类名字不能和已存在的分类名字相同");
        }
        categoryService.updateById(category);
        return Result.succ("更新分类成功");
    }

    /**
     * @Author wwx
     * @Description  根据id删除分类
     * @Date 2021/3/21 21:35
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    // @RequestParam /deleteCategory?id=参数值
    // @PathVariable /deleteCategory/参数值
    @OperationLogger("删除分类")
    @GetMapping("/deleteCategory")
    public Result deleteCategory(@RequestParam Long id){
        //删除存在博客关联的分类后，该博客的查询会出异常
        int count = blogService.count(new QueryWrapper<Blog>().eq("category_id", id));
        if (count != 0){
            return Result.fail("该分类下还有博客，不可删除");
        }

        categoryService.removeById(id);
        return Result.succ("删除分类成功");
    }

}
