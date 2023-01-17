package com.luna.his.org.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.core.framework.HisServiceSupport;
import com.luna.his.org.mapper.ScheduleFixedMapper;
import com.luna.his.org.model.ScheduleFixed;
import com.luna.his.org.service.dto.ScheduleFixedDTO;
import org.springframework.stereotype.Service;

@Service
public class ScheduleFixedService extends HisServiceSupport<ScheduleFixed, ScheduleFixedMapper> {
    /**
     * 新增或更新排班
     *
     * @param scheduleDTO
     */
    public void saveOrUpdateSchedule(ScheduleFixedDTO scheduleDTO) {
        boolean isRest = scheduleDTO.getIsRest();
        boolean isNormal = scheduleDTO.getIsNormal();
        if (isRest) {
            if (isNormal) {
                throw new BusinessException("不能同时设置休息与正常班");
            }
            if (scheduleDTO.getShift1() != null || scheduleDTO.getShift2() != null) {
                throw new BusinessException("设置了休息后不能设置其他班次");
            }
        }
        ScheduleFixed schedule = new ScheduleFixed();
        SimpleBeanCopyUtil.simpleCopy(scheduleDTO, schedule);

        // TODO 考虑实际情况，根据更新还是新增多来处理下面逻辑

        int effect = getSqlMapper().update(schedule, new LambdaQueryWrapper<ScheduleFixed>()
                .eq(ScheduleFixed::getEmployeeId, scheduleDTO.getEmployeeId())
                .eq(ScheduleFixed::getHospitalId, scheduleDTO.getHospitalId())
                .eq(ScheduleFixed::getWeekday, scheduleDTO.getWeekday())
        );

        if (effect <= 0) {
            save(schedule);
        }
    }
}