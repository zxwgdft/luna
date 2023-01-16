package com.luna.his.schedule.mapper;

import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.his.schedule.model.Schedule;
import com.luna.his.schedule.service.dto.ScheduleQuery;
import com.luna.his.schedule.service.vo.ScheduleVO;

import java.util.List;

public interface ScheduleMapper extends CommonMapper<Schedule> {

    /**
     * 查询排班
     *
     * @param query
     * @return
     */
    List<ScheduleVO> findSchedule(ScheduleQuery query);

    /**
     * 查询固定排班
     *
     * @param query
     * @return
     */
    List<ScheduleVO> findFixedSchedule(ScheduleQuery query);

}