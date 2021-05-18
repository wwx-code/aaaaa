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
import top.wwxyh.entity.Visitor;
import top.wwxyh.service.VisitorService;

/**
 * @Description:  访客统计
 * @Author: wwx
 * @Date: 2021/3/27 15:55
 */
@RestController
@RequestMapping("/admin")
public class VisitorAdminController {
    @Autowired
    VisitorService visitorService;

    /**
     * @Author wwx
     * @Description  分页查询访客列表
     * @Date 2021/3/27 15:59
     * @Param [pageNum, pageSize, data]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/visitors")
    public Result visitors(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String[] data){
        Page<Visitor> page = new Page<>(pageNum,pageSize);
        //设置查询套件构造器
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String startDate = null;
        String endDate = null;
        if (data.length == 2){
            startDate = data[0];
            endDate = data[1];
            queryWrapper.between("last_time",startDate,endDate);
        }

        IPage<Visitor> pageInfo = visitorService.page(page, queryWrapper);
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  根据id删除访客列表
     * @Date 2021/3/27 16:00
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/visitor/delete")
    public Result deleteById(@RequestParam Long id){
        visitorService.removeById(id);
        return Result.succ("删除成功");
    }
}
