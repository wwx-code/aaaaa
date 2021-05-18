package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.BlogTag;
import top.wwxyh.entity.Tag;
import top.wwxyh.service.BlogTagService;
import top.wwxyh.service.TagService;
import top.wwxyh.util.StringUtils;

/**
 * @Description: 博客标签后台管理
 * @Author: wwx
 * @Date: 2021/3/21 22:53
 */
@RestController
@RequestMapping("/admin")
public class TagAdminController {

    @Autowired
    TagService tagService;

    @Autowired
    BlogTagService blogTagService;

    /**
     * @Author wwx
     * @Description  获取博客标签列表
     * @Date 2021/3/22 10:11
     * @Param [pageNum]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage pageInfo = tagService.page(page, new QueryWrapper<Tag>().orderByDesc("id"));
        return Result.succ("获取博客标签列表成功",pageInfo);
    }

    /**
     * @Author wwx
     * @Description  新增标签
     * @Date 2021/3/22 10:21
     * @Param [tag]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("添加标签")
    @PostMapping("/addTag")
    public Result addTag(@RequestBody Tag tag){
        if (StringUtils.isEmpty(tag.getTagName())){
            return Result.fail("标签名称不能为空");
        }

        //查询标签是否已存在
        Tag tag1 = tagService.getOne(new QueryWrapper<Tag>().eq("tag_name", tag.getTagName()));
        if (tag1 != null){
            return Result.fail("该标签已存在，请勿重复添加");
        }

        tagService.save(tag);
        return Result.succ("新增标签成功");
    }

    /**
     * @Author wwx
     * @Description  更新标签
     * @Date 2021/3/22 10:23
     * @Param [tag]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新标签")
    @PostMapping("/updateTag")
    public Result updateTag(@RequestBody Tag tag){
        if (StringUtils.isEmpty(tag.getTagName())){
            return Result.fail("标签名称不能为空");
        }

        //查询标签是否已存在
        Tag tag1 = tagService.getOne(new QueryWrapper<Tag>().eq("tag_name", tag.getTagName()));
        if (tag1 != null){
            return Result.fail("该标签已存在");
        }

        tagService.updateById(tag);
        return Result.succ("更新标签成功");
    }

    /**
     * @Author wwx
     * @Description  删除标签
     * @Date 2021/3/22 10:35
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("删除标签")
    @GetMapping("/deleteTag")
    public Result deleteTag(@RequestParam Long id){
        //删除存在博客关联的标签后，该博客的查询会出异常
        int count = blogTagService.count(new QueryWrapper<BlogTag>().eq("tag_id", id));
        if (count != 0){
            return Result.fail("该标签下还有博客，不可删除");
        }

        tagService.removeById(id);
        return Result.succ("删除标签成功");
    }
}
