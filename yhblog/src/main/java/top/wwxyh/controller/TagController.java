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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class TagController {

    @Autowired
    BlogService blogService;

    /**
     * @Author wwx
     * @Description  根据标签name查询公开博客List  同样有bug
     * @Date 2021/3/29 9:23
     * @Param [pageNum, tagName]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "查看标签")
    @GetMapping("/tagName")
    public Result tagName(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam String tagName) {
        Page page = new Page(pageNum, 5);
        IPage pageInfo = blogService.getBlogInfoListByTagNameAndIsPublished(page, tagName);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据标签id查询公开博客List
     * @Date 2021/3/29 9:24
     * @Param [pageNum, id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/tag")
    public Result tag(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam Long id){
        Page page = new Page(pageNum, 5);
        IPage pageInfo = blogService.getBlogInfoListByTagIdAndIsPublished(page, id);
        return Result.succ(pageInfo);
    }
}
