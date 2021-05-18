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
import top.wwxyh.entity.OperationLog;
import top.wwxyh.service.OperationLogService;

/**
 * @Description:  操作日志后台管理
 * @Author: wwx
 * @Date: 2021/3/26 17:16
 */
@RestController
@RequestMapping("/admin")
public class OperationLogController {
    @Autowired
    OperationLogService operationLogService;

    /**
     * @Author wwx
     * @Description  分页查询操作日志列表
     * @Date 2021/3/26 17:33
     * @Param [pageNum, pageSize, Data]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/operationLogs")
    public Result operationLogs(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "") String[] date){
        Page<OperationLog> page = new Page<>(pageNum,pageSize);
        //设置查询套件构造器
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String startDate = null;
        String endDate = null;
        if (date.length == 2){
            startDate = date[0];
            endDate = date[1];
            queryWrapper.between("create_time",startDate,endDate);
        }

        IPage<OperationLog> pageInfo = operationLogService.page(page,queryWrapper);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据id删除操作日志
     * @Date 2021/3/26 19:03
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/operationLog/delete")
    public Result deleteById(@RequestParam Long id){
        operationLogService.removeById(id);
        return Result.succ("删除成功");
    }
}
