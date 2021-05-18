package top.wwxyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.wwxyh.common.vo.CategoryBlogCount;
import top.wwxyh.common.vo.TagBlogCount;
import top.wwxyh.entity.Category;
import top.wwxyh.entity.Tag;
import top.wwxyh.mapper.*;
import top.wwxyh.service.DashboardService;

import java.util.List;

/**
 * @Description: 仪表盘实现类
 * @Author: wwx
 * @Date: 2021/4/18 0:40
 */
@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    VisitLogMapper visitLogMapper;

    @Override
    public List getCategoryBlogCountList() {
        //查询每个分类的博客数量
        List<CategoryBlogCount> categoryBlogCountList = blogMapper.getCategoryBlogCountList();
        //查询所有分类的信息
        List<Category> categoryList = categoryMapper.selectList(new QueryWrapper<Category>().orderByDesc("id"));

        for (Category category : categoryList) {
            Long id = category.getId();
            for (CategoryBlogCount categoryBlogCount : categoryBlogCountList) {
                Long categoryId = categoryBlogCount.getId();
                if (categoryId == id){
                    categoryBlogCount.setName(category.getCategoryName());
                    break;
                }
            }
        }
        return categoryBlogCountList;
    }

    @Override
    public List getTagBlogCountList() {
        //查询每个标签的博客数量
        List<TagBlogCount> tagBlogCountList = tagMapper.getTagBlogCount();
        //查询所有标签
        List<Tag> tagList = tagMapper.selectList(new QueryWrapper<Tag>().orderByDesc("id"));

        for (Tag tag : tagList) {
            Long id = tag.getId();
            for (TagBlogCount tagBlogCount : tagBlogCountList) {
                Long tagId = tagBlogCount.getId();
                if (tagId == id){
                    tagBlogCount.setName(tag.getTagName());
                    break;
                }
            }
        }
        return tagBlogCountList;
    }
}
