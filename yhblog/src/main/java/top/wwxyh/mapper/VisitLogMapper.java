package top.wwxyh.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;
import top.wwxyh.entity.VisitLog;
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
public interface VisitLogMapper extends BaseMapper<VisitLog> {

    //查询访客日志
    IPage<VisitLog> getVisitLogListByUUIDAndDate(Page<VisitLog> page, String uuid, String startDate, String endDate);
}
