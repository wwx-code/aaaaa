package top.wwxyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.common.dto.BlogAdmin;
import top.wwxyh.common.vo.BlogDetail;
import top.wwxyh.common.vo.BlogIdAndTitle;
import top.wwxyh.common.vo.BlogInfo;
import top.wwxyh.common.vo.RandomBlog;
import top.wwxyh.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wwxyh.mapper.BlogMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
public interface BlogService extends IService<Blog> {

    //后台管理页面博客列表查询（根据标题和文章分类）
    Page<BlogAdmin> getListByTitleAndCategoryId(Page<BlogAdmin> page, String title, Integer categoryId);

    //博客首页按置顶、创建时间排序 分页查询博客简要信息列表
    IPage<BlogInfo> getBlogInfoByIsPublish(Page<BlogInfo> page, Integer pageNum);

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

    //查询博客归档信息
    Map<String, Object> getArchiveBlogAndCountByIsPublished();

    //后台管理评论页面查询博客id和标题
//    List<BlogIdAndTitle> getBlogIdAndTitle();

    //首页查询公开且推荐的博客
    List<BlogIdAndTitle> getNewBlogListByIsPublishedAndIsRecommend();

    //随机博客展示
    List<RandomBlog> getRandomBlogListByLimitNumAndIsPublished();
}
