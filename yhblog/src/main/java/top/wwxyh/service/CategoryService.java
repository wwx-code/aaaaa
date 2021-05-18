package top.wwxyh.service;

import top.wwxyh.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
public interface CategoryService extends IService<Category> {


    //获取所有分类List
    // .list()

    //获取所有分类List不查询id

    //添加分类
    // .save

    //按id查询分类
    // .getById

    //按name查询分类
    // .getOne(Wrapper<T> queryWrapper.eq("name", name))

    //按id删除分类
    // .removeById

    //更新分类
    // .saveOrUpdate
}
