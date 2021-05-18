package top.wwxyh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.About;
import top.wwxyh.service.AboutService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class AboutController {
    @Autowired
    AboutService aboutService;

    /**
     * @Author wwx
     * @Description  获取关于我页面信息
     * @Date 2021/4/14 0:25
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "访问页面", content = "关于我")
    @GetMapping("/about")
    public Result about() {
        About about = aboutService.getOne(new QueryWrapper<About>().eq("belong", "about"));
        return Result.succ(about);
    }
}
