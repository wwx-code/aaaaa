package top.wwxyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.wwxyh.entity.VisitLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
public interface VisitLogService extends IService<VisitLog> {

    //查询访客日志
    IPage<VisitLog> getVisitLogListByUUIDAndDate(Page<VisitLog> page, String uuid, String startDate, String endDate);
}
