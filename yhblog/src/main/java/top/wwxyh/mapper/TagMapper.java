package top.wwxyh.mapper;

import org.springframework.stereotype.Repository;
import top.wwxyh.common.vo.TagBlogCount;
import top.wwxyh.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    //按博客id查询标签集合
    List<Tag> getTagListByBlogId(Long blogId);

    //查询每个标签的博客数量
    List<TagBlogCount> getTagBlogCount();
}
