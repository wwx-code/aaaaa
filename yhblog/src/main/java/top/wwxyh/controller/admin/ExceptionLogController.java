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
import top.wwxyh.entity.ExceptionLog;
import top.wwxyh.service.ExceptionLogService;

/**
 * @Description:  异常日志后台管理
 * @Author: wwx
 * @Date: 2021/3/26 19:04
 */
@RestController
@RequestMapping("/admin")
public class ExceptionLogController {

    @Autowired
    ExceptionLogService exceptionLogService;

    /**
     * @Author wwx
     * @Description  分页查询异常日志列表
     * @Date 2021/3/26 19:08
     * @Param [pageNum, pageSize, data]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/exceptionLogs")
    public Result exceptionLogs(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "") String[] date){
        Page<ExceptionLog> page = new Page<>(pageNum,pageSize);
        //设置查询套件构造器
        QueryWrapper<ExceptionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String startDate = null;
        String endDate = null;
        if (date.length == 2){
            startDate = date[0];
            endDate = date[1];
            queryWrapper.between("create_time",startDate,endDate);
        }

        IPage<ExceptionLog> pageInfo = exceptionLogService.page(page,queryWrapper);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据id删除异常日志
     * @Date 2021/3/26 19:10
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/exceptionLog/delete")
    public Result deleteById(@RequestParam Long id){
        exceptionLogService.removeById(id);
        return Result.succ("删除成功");
    }
}
