package top.wwxyh.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wwxyh.annotation.OperationLogger;
import top.wwxyh.common.dto.AboutDto;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.About;
import top.wwxyh.entity.Friend;
import top.wwxyh.service.AboutService;
import top.wwxyh.service.FriendService;
import top.wwxyh.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @Description: 友人帐后台管理
 * @Author: wwx
 * @Date: 2021/3/26 16:04
 */
@RestController
@RequestMapping("/admin")
public class FriendAdminController {

    @Autowired
    FriendService friendService;

    @Autowired
    AboutService aboutService;

    /**
     * @Author wwx
     * @Description  分页获取友链列表
     * @Date 2021/3/26 16:21
     * @Param [pageNum, pageSize]
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/friends")
    public Result friends(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize){
        Page<Friend> page = new Page<>(pageNum,pageSize);
        IPage<Friend> pageInfo = friendService.page(page, new QueryWrapper<Friend>().orderByDesc("create_time"));
        return Result.succ(pageInfo);
    }

    /**
     * @Author wwx
     * @Description  更新友链公开状态
     * @Date 2021/3/26 16:25
     * @Param [id, isPublished]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("更新友链公开状态")
    @GetMapping("/friend/published")
    public Result updatePublished(@RequestParam Long id, @RequestParam boolean isPublished){
        friendService.update(new UpdateWrapper<Friend>().eq("id",id).set("is_published",isPublished));
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  根据id删除友链
     * @Date 2021/3/26 16:27
     * @Param [id]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("删除友链")
    @GetMapping("/friend/delete")
    public Result deleteById(@RequestParam Long id){
        friendService.removeById(id);
        return Result.succ("删除成功");
    }

    /**
     * @Author wwx
     * @Description  新增或更新友链
     * @Date 2021/3/26 16:44
     * @Param [friend]
     * @return top.wwxyh.common.lang.Result
     **/
    @OperationLogger("添加修改友链")
    @PostMapping("/friend/edit")
    public Result saveOrUpdateFriend(@Validated @RequestBody Friend friend){
        if (friend.getId() == null){
            //没有id，新增友链，初始化浏览次数为0同时设置创建时间
            friend.setViews(0);
            friend.setCreateTime(LocalDateTime.now());
        }
        friendService.saveOrUpdate(friend);
        return Result.succ("更新成功");
    }

    /**
     * @Author wwx
     * @Description  获取友链页面信息
     * @Date 2021/4/13 22:14
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/friendInfo")
    public Result friendInfo(){
        About info = aboutService.getOne(new QueryWrapper<About>().eq("belong", "friend"));
        return Result.succ(info);
    }

    /**
     * @Author wwx
     * @Description  更新友链页面信息
     * @Date 2021/4/13 22:16
     * @Param [aboutDto]
     * @return top.wwxyh.common.lang.Result
     **/
    @PostMapping("/updateFriendInfo")
    public Result updateFriendInfo(@RequestBody AboutDto aboutDto){
        UpdateWrapper<About> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("belong","friend")
                .set("content",aboutDto.getContent());

        aboutService.update(updateWrapper);
        return Result.succ("友链页面信息更新成功");
    }
}
