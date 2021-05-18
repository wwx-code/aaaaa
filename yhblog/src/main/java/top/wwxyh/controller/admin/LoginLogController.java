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
import top.wwxyh.entity.LoginLog;
import top.wwxyh.entity.Visitor;
import top.wwxyh.service.LoginLogService;

/**
 * @Description:  登录日志后台管理
 * @Author: wwx
 * @Date: 2021/3/27 16:05
 */
@RestController
@RequestMapping("/admin")
public class LoginLogController {

    @Autowired
    LoginLogService loginLogService;

    /**
     * @Author wwx
     * @Description  分页查询登录日志
     * @Date 2021/3/27 16:08
     * @Param [pageNum, pageSize, data]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/loginLogs")
    public Result loginLogs(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String[] date) {
        Page<LoginLog> page = new Page<>(pageNum,pageSize);
        //设置查询套件构造器
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String startDate = null;
        String endDate = null;
        if (date.length == 2){
            startDate = date[0];
            endDate = date[1];
            queryWrapper.between("create_time",startDate,endDate);
        }

        IPage<LoginLog> pageInfo = loginLogService.page(page, queryWrapper);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据id删除登录日志
     * @Date 2021/3/27 16:07
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/loginLog/delete")
    public Result deleteById(@RequestParam Long id){
        loginLogService.removeById(id);
        return Result.succ("删除成功");
    }
}
