package top.wwxyh.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javafx.scene.control.Pagination;
import org.springframework.stereotype.Repository;
import top.wwxyh.common.dto.BlogAdmin;
import top.wwxyh.common.vo.*;
import top.wwxyh.entity.Blog;
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
public interface BlogMapper extends BaseMapper<Blog> {

    //后台管理页面博客列表查询（根据标题和文章分类）
    List<BlogAdmin> getListByTitleAndCategoryId(Page<BlogAdmin> page, String title, Integer categoryId);

    //博客首页按置顶、创建时间排序 分页查询博客简要信息列表
    IPage<BlogInfo> getBlogInfoByIsPublish(Page<BlogInfo> page);

    //按id获取公开博客详情
    BlogDetail getBlogByIdAndIsPublished(Long id);

    BlogAdmin getBlogById(Long id);

    //根据分类查询博客
    //博客分类页面按分类名称查询公开博客List
    IPage<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(Page<BlogInfo> page, String categoryName);
    //博客分类页面按分类id查询公开博客List
    IPage<BlogInfo> getBlogInfoListByCategoryIdAndIsPublished(Page<BlogInfo> page, Integer categoryId);

    //根据标签查询博客
    //根据标签name查询公开博客List
    IPage<BlogInfo> getBlogInfoListByTagNameAndIsPublished(Page<BlogInfo> page, String tagName);
    //根据标签id查询公开博客List
    IPage<BlogInfo> getBlogInfoListByTagIdAndIsPublished(Page<BlogInfo> page, Long id);

    //查询博客归档
    //查询博客发布年月
    List<String> getGroupYearMonthByIsPublished();
    //查询指定年月的博客id，标题
    List<ArchiveBlog> getArchiveBlogListByYearMonthAndIsPublished(String yearMonth);

    //首页查询公开且推荐的博客
    List<BlogIdAndTitle> getNewBlogListByIsPublishedAndIsRecommend();

    //随机博客展示
    List<RandomBlog> getRandomBlogListByLimitNumAndIsPublished(Integer limitNum);

    //查询每个分类的博客数量
    List<CategoryBlogCount> getCategoryBlogCountList();
}
