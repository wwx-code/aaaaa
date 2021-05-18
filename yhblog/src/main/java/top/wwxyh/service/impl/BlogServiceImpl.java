package top.wwxyh.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.common.dto.BlogAdmin;
import top.wwxyh.common.vo.*;
import top.wwxyh.entity.Blog;
import top.wwxyh.mapper.BlogMapper;
import top.wwxyh.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.wwxyh.service.TagService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    BlogMapper blogMapper;
    @Autowired
    TagService tagService;

    //随机博客显示5条
    private static final int randomBlogLimitNum = 5;

    @Override
    public Page<BlogAdmin> getListByTitleAndCategoryId(Page<BlogAdmin> page, String title, Integer categoryId) {
        return page.setRecords(blogMapper.getListByTitleAndCategoryId(page, title, categoryId));
    }

    @Override
    public IPage<BlogInfo> getBlogInfoByIsPublish(Page<BlogInfo> page, Integer pageNum) {
        IPage<BlogInfo> blogInfo = blogMapper.getBlogInfoByIsPublish(page);
        for (BlogInfo record : blogInfo.getRecords()) {
            record.setTags(tagService.getTagListByBlogId(record.getId()));
        }
        return blogInfo;
    }

    @Override
    public BlogDetail getBlogByIdAndIsPublished(Long id) {
        BlogDetail blogDetail = blogMapper.getBlogByIdAndIsPublished(id);
        Assert.notNull(blogDetail,"该博客已删除");
        return blogDetail;
    }

    @Override
    public BlogAdmin getBlogById(Long id) {
        BlogAdmin blog = blogMapper.getBlogById(id);
        return blog;
    }

    @Override
    public IPage<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(Page<BlogInfo> page, String categoryName) {
        IPage<BlogInfo> blogInfo = blogMapper.getBlogInfoListByCategoryNameAndIsPublished(page, categoryName);
        for (BlogInfo record : blogInfo.getRecords()) {
            record.setTags(tagService.getTagListByBlogId(record.getId()));
        }
        return blogInfo;
    }

    @Override
    public IPage<BlogInfo> getBlogInfoListByCategoryIdAndIsPublished(Page<BlogInfo> page, Integer categoryId) {
        IPage<BlogInfo> blogInfo = blogMapper.getBlogInfoListByCategoryIdAndIsPublished(page, categoryId);
        for (BlogInfo record : blogInfo.getRecords()) {
            record.setTags(tagService.getTagListByBlogId(record.getId()));
        }
        return blogInfo;
    }

    @Override
    public IPage<BlogInfo> getBlogInfoListByTagNameAndIsPublished(Page<BlogInfo> page, String tagName) {
        IPage<BlogInfo> blogInfo = blogMapper.getBlogInfoListByTagNameAndIsPublished(page, tagName);
        for (BlogInfo record : blogInfo.getRecords()) {
            record.setTags(tagService.getTagListByBlogId(record.getId()));
        }
        return blogInfo;
    }

    @Override
    public IPage<BlogInfo> getBlogInfoListByTagIdAndIsPublished(Page<BlogInfo> page, Long id) {
        return blogMapper.getBlogInfoListByTagIdAndIsPublished(page, id);
    }

    @Override
    public Map<String, Object> getArchiveBlogAndCountByIsPublished() {
        Map<String, Object> map = new HashMap<>();
        //获取公开博客年月List
        List<String> groupYearMonth = blogMapper.getGroupYearMonthByIsPublished();
        Map<String, List<ArchiveBlog>> archiveBlogMap = new LinkedHashMap<>();
        for (String s : groupYearMonth) {
            //按年月查询公开博客简要信息List
            List<ArchiveBlog> archiveBlogs = blogMapper.getArchiveBlogListByYearMonthAndIsPublished(s);
            for (ArchiveBlog archiveBlog : archiveBlogs) {
                if (!"".equals(archiveBlog.getPassword())) {
                    archiveBlog.setPrivacy(true);
                    archiveBlog.setPassword("");
                } else {
                    archiveBlog.setPrivacy(false);
                }
            }
            archiveBlogMap.put(s,archiveBlogs);
        }
        Integer count = blogMapper.selectCount(new QueryWrapper<Blog>().eq("is_published", true));
        map.put("blogMap",archiveBlogMap);
        map.put("count",count);
        return map;
    }

    @Override
    public List<BlogIdAndTitle> getNewBlogListByIsPublishedAndIsRecommend() {
        List<BlogIdAndTitle> newBlogList = blogMapper.getNewBlogListByIsPublishedAndIsRecommend();
        return newBlogList;
    }

    @Override
    public List<RandomBlog> getRandomBlogListByLimitNumAndIsPublished() {
        List<RandomBlog> randomBlogs = blogMapper.getRandomBlogListByLimitNumAndIsPublished(randomBlogLimitNum);
        for (RandomBlog randomBlog : randomBlogs) {
            randomBlog.setTags(tagService.getTagListByBlogId(randomBlog.getId()));
        }
        return randomBlogs;
    }

}
