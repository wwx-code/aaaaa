package top.wwxyh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.Comment;
import top.wwxyh.entity.Moment;
import top.wwxyh.service.MomentService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class MomentController {

    @Autowired
    MomentService momentService;

    /**
     * @Author wwx
     * @Description  分页查询动态List
     * @Date 2021/3/28 16:30
     * @Param [pageNum]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "访问页面", content = "动态")
    @GetMapping("/moments")
    public Result moments(@RequestParam(defaultValue = "1") Integer pageNum){
        Page page = new Page(pageNum, 5);
        IPage pageInfo = momentService.page(page, new QueryWrapper<Moment>().orderByDesc("create_time"));
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  点赞动态
     * @Date 2021/3/28 16:41
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "点赞动态")
    @PostMapping("/moment/like")
    public Result like(@RequestParam Long id){
        Moment moment = momentService.getById(id);
        Integer currentLikes = moment.getLikes();

        UpdateWrapper<Moment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("likes",currentLikes+1);
        momentService.update(updateWrapper);
        return Result.succ("点赞成功");
    }
}
