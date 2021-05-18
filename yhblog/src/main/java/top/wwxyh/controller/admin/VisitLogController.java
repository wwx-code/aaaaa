package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.VisitLog;
import top.wwxyh.service.VisitLogService;

/**
 * @Description:  访问日志后台管理
 * @Author: wwx
 * @Date: 2021/3/27 15:37
 */
@RestController
@RequestMapping("/admin")
public class VisitLogController {

    @Autowired
    VisitLogService visitLogService;

    /**
     * @Author wwx
     * @Description  分页查询操作日志列表
     * @Date 2021/3/27 15:46
     * @Param [pageNum, pageSize, data]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/visitorLogs")
    public Result visitorLogs(@RequestParam(defaultValue = "") String uuid,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String[] date){
        Page<VisitLog> page = new Page<>(pageNum,pageSize);
        //设置查询套件构造器
//        QueryWrapper<VisitLog> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("create_time");

        String startDate = null;
        String endDate = null;
        if (date.length == 2){
            startDate = date[0];
            endDate = date[1];
//            queryWrapper.between("create_time",startDate,endDate);
        }

        IPage<VisitLog> pageInfo = visitLogService.getVisitLogListByUUIDAndDate(page, uuid, startDate, endDate);
//        IPage<VisitLog> pageInfo = visitLogService.page(page, queryWrapper);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据id删除访问日志
     * @Date 2021/3/27 15:51
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/visitorLog/delete")
    public Result deleteById(@RequestParam Long id){
        visitLogService.removeById(id);
        return Result.succ("删除成功");
    }
}
