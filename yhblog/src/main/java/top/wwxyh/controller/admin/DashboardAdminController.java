package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.VisitLog;
import top.wwxyh.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:  仪表盘
 * @Author: wwx
 * @Date: 2021/4/17 23:42
 */
@RestController
@RequestMapping("/admin")
public class DashboardAdminController {

    @Autowired
    VisitLogService visitLogService;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DashboardService dashboardService;

    /**
     * @Author wwx
     * @Description  首页仪表盘数据
     * @Date 2021/4/18 2:26
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/dashbord")
    public Result dashbord() {
        //获取当前日期
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = format.format(date);

        //今日访客量（今日PV）
        int todayPV = visitLogService.count(new QueryWrapper<VisitLog>().eq("date(create_time)", currentDate));
        //文章数
        int blogCount = blogService.count();
        //评论数
        int commentCount = commentService.count();
        //分类下博客数量
        List categoryBlogCountList = dashboardService.getCategoryBlogCountList();
        //标签下博客数量
        List tagBlogCountList = dashboardService.getTagBlogCountList();

        HashMap<String, Object> map = new HashMap<>();
        map.put("pv", todayPV);
        map.put("blogCount", blogCount);
        map.put("commentCount", commentCount);
        map.put("category", categoryBlogCountList);
        map.put("tag", tagBlogCountList);
        return Result.succ(map);
    }
}
