package top.wwxyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.entity.User;
import top.wwxyh.mapper.UserMapper;
import top.wwxyh.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
