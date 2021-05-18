package top.wwxyh.service;

import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.mapper.BlogMapper;

import java.util.List;
import java.util.Map;

/**
 * @Description: 仪表盘服务类
 * @Author: wwx
 * @Date: 2021/4/18 0:32
 */
public interface DashboardService {
    List getCategoryBlogCountList();

    List getTagBlogCountList();
}
