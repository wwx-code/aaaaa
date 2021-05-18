package top.wwxyh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.service.BlogService;

import java.util.Map;

/**
 * @Description:  归档页面
 * @Author: wwx
 * @Date: 2021/3/28 18:05
 */
@RestController
public class ArchiveController {

    @Autowired
    BlogService blogService;

    /**
     * @Author wwx
     * @Description  按年月分组归档公开博客 统计公开博客总数
     * @Date 2021/3/28 21:27
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "访问页面", content = "文章归档")
    @GetMapping("/archives")
    public Result archives(){
        Map<String, Object> archiveBlogMap = blogService.getArchiveBlogAndCountByIsPublished();
        return Result.succ(archiveBlogMap);
    }
}
