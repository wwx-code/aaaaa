package top.wwxyh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.wwxyh.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
