package top.wwxyh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.wwxyh.entity.VisitLog;
import top.wwxyh.mapper.VisitLogMapper;
import top.wwxyh.service.VisitLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwxyh
 * @since 2021-03-18
 */
@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements VisitLogService {

    @Autowired
    VisitLogMapper visitLogMapper;

    @Override
    public IPage<VisitLog> getVisitLogListByUUIDAndDate(Page<VisitLog> page, String uuid, String startDate, String endDate) {
        return visitLogMapper.getVisitLogListByUUIDAndDate(page,uuid,startDate,endDate);
    }
}
