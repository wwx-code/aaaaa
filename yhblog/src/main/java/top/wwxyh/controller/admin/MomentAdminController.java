package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.Moment;
import top.wwxyh.service.MomentService;

import java.time.LocalDateTime;

/**
 * @Description: 博客动态后台管理
 * @Author: wwx
 * @Date: 2021/3/24 13:26
 */
@RestController
@RequestMapping("/admin")
public class MomentAdminController {

    @Autowired
    MomentService momentService;

    /**
     * @Author wwx
     * @Description  分页查询动态列表
     * @Date 2021/3/24 14:52
     * @Param [pageNum, pageSize]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/moments")
    public Result moments(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        IPage pageInfo = momentService.page(page, new QueryWrapper<Moment>().orderByDesc("create_time"));
        return Result.succ("获取动态列表成功",pageInfo);
    }

    /**
     * @Author wwx
     * @Description  修改动态公开状态
     * @Date 2021/3/24 14:58
     * @Param [id, isPublished]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新动态公开状态")
    @GetMapping("/moment/publish")
    public Result updatePublish(@RequestParam Long id, @RequestParam boolean isPublished){
        momentService.update(new UpdateWrapper<Moment>().eq("id",id).set("is_published",isPublished));
        return Result.succ("修改动态发布状态成功");
    }

    /**
     * @Author wwx
     * @Description  根据id删除动态
     * @Date 2021/3/24 15:01
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("删除动态")
    @GetMapping("/moment/delete")
    public Result deleteMomentById(@RequestParam Long id){
        momentService.removeById(id);
        return Result.succ("删除动摇成功");
    }

    /**
     * @Author wwx
     * @Description  根据id查询动态
     * @Date 2021/3/24 15:04
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/moment")
    public Result getMomentById(@RequestParam Long id){
        Moment moment = momentService.getById(id);
        return Result.succ(moment);
    }

    /**
     * @Author wwx
     * @Description  更新或增加动态
     * @Date 2021/3/24 15:15
     * @Param [moment]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("发布/更新动态")
    @PostMapping("/moment/edit")
    public Result saveMoment(@RequestBody Moment moment){
        //若动态没有添加创建时间，则取当前时间
        if (moment.getCreateTime() == null){
            moment.setCreateTime(LocalDateTime.now());
        }
        momentService.saveOrUpdate(moment);
        return Result.succ("保存动态成功");
    }
}
