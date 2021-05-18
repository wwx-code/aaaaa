package top.wwxyh.service;

import top.wwxyh.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
public interface TagService extends IService<Tag> {

    //根据博客id获取标签列表
    List<Tag> getTagListByBlogId(Long blogId);
}
