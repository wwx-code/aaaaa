package top.wwxyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.entity.Tag;
import top.wwxyh.mapper.TagMapper;
import top.wwxyh.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    TagMapper tagMapper;

    @Override
    public List<Tag> getTagListByBlogId(Long blogId) {
        return tagMapper.getTagListByBlogId(blogId);
    }
}
