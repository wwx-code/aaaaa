package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.dto.AboutDto;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.About;
import top.wwxyh.service.AboutService;

/**
 * @Description: 关于我后台管理
 * @Author: wwx
 * @Date: 2021/4/13 21:58
 */
@RestController
@RequestMapping("/admin")
public class AboutAdminController {
    @Autowired
    AboutService aboutService;

    /**
     * @Author wwx
     * @Description  查询关于我信息
     * @Date 2021/4/13 22:11
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/about")
    public Result about(){
        About info = aboutService.getOne(new QueryWrapper<About>().eq("belong", "about"));
        return Result.succ(info);
    }

    /**
     * @Author wwx
     * @Description  更新关于我信息
     * @Date 2021/4/13 22:12
     * @Param [aboutDto]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("修改关于我页面")
    @PostMapping("/updateAbout")
    public Result updateAbout(@RequestBody AboutDto aboutDto){
        UpdateWrapper<About> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("belong","about")
                .set("title",aboutDto.getTitle())
                .set("content",aboutDto.getContent());

        aboutService.update(updateWrapper);
        return Result.succ("关于我信息更新成功");
    }
}
