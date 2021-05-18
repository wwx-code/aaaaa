package top.wwxyh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.wwxyh.annotation.VisitLogger;
import top.wwxyh.common.lang.Result;
import top.wwxyh.entity.About;
import top.wwxyh.entity.Friend;
import top.wwxyh.service.AboutService;
import top.wwxyh.service.FriendService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@RestController
public class FriendController {

    @Autowired
    FriendService friendService;

    @Autowired
    AboutService aboutService;

    /**
     * @Author wwx
     * @Description  获取友链页面
     * @Date 2021/3/29 10:14
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "访问页面", content = "友链")
    @GetMapping("/friends")
    public Result friends(){
        List<Friend> friendList = friendService.list(new QueryWrapper<Friend>().eq("is_published", true));
        //友链描述未添加，后续加入

        return Result.succ(friendList);
    }

    /**
     * @Author wwx
     * @Description  友链列表获取友链页面信息
     * @Date 2021/4/14 0:23
     * @Param []
     * @return top.wwxyh.common.lang.Result
     **/
    @GetMapping("/friendInfo")
    public Result friendInfo(){
        About friendInfo = aboutService.getOne(new QueryWrapper<About>().eq("belong", "friend"));
        return Result.succ(friendInfo);
    }

    /**
     * @Author wwx
     * @Description  按昵称增加友链浏览次数
     * @Date 2021/3/29 10:18
     * @Param [nickname]
     * @return top.wwxyh.common.lang.Result
     **/
    @VisitLogger(behavior = "点击友链")
    @PostMapping("/addViews")
    public Result addViews(@RequestParam String nickname){
        Friend friend = friendService.getOne(new QueryWrapper<Friend>().eq("nickname", nickname));
        Integer views = friend.getViews();

        UpdateWrapper<Friend> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("nickname", nickname).set("views",views+1);
        friendService.update(updateWrapper);
        return Result.succ(null);
    }
}
