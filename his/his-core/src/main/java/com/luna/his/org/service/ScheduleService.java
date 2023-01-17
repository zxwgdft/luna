package com.luna.his.org.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.utils.TimeUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.core.framework.HisServiceSupport;
import com.luna.his.core.util.MultiIdUtil;
import com.luna.his.org.model.Employee;
import com.luna.his.org.mapper.ScheduleMapper;
import com.luna.his.org.model.Schedule;
import com.luna.his.org.service.dto.ScheduleDTO;
import com.luna.his.org.service.dto.ScheduleQuery;
import com.luna.his.org.service.vo.FixedScheduleVO;
import com.luna.his.org.service.vo.MonthScheduleVO;
import com.luna.his.org.service.vo.ScheduleVO;
import com.luna.his.org.service.vo.WeekScheduleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

/**
 * 排班服务
 *
 * @author TontoZhou
 */
@Service
@RequiredArgsConstructor
public class ScheduleService extends HisServiceSupport<Schedule, ScheduleMapper> {

    private final EmployeeService employeeService;

    /**
     * 获取周排班信息
     *
     * @param query
     * @return
     */
    public WeekScheduleVO getWeekSchedule(ScheduleQuery query) {
        // 设置查询周
        query.setWeek(TimeUtil.getWeekBasedYear(query.getDate()));
        query.setMonth(null);

        // TODO 考虑workScope情况，在确认多诊所如何处理后需要修改
        List<Employee> employees = employeeService.findSimpleList(
                new LambdaQueryWrapper<Employee>()
                        .eq(query.getJob() != null, Employee::getJob, query.getJob())
                        .eq(query.getEmployeeId() != null, Employee::getId, query.getEmployeeId())
                        .like(Employee::getWorkHospitalIds, MultiIdUtil.getLikeSql(query.getHospitalId()))
        );

        if (employees.size() > 0) {
            List<ScheduleVO> schedules = getSqlMapper().findSchedule(query);
            List<ScheduleVO> fixedSchedules = getSqlMapper().findFixedSchedule(query);
            return new WeekScheduleVO(employees, schedules, fixedSchedules);
        }

        // 返回空
        return new WeekScheduleVO();
    }

    /**
     * 获取周排班信息
     *
     * @param query
     * @return
     */
    public MonthScheduleVO getMonthSchedule(ScheduleQuery query) {
        // 设置查询周
        query.setMonth(getMonth(TimeUtil.toLocalDate(query.getDate())));
        query.setWeek(null);

        // TODO 考虑workScope情况，在确认多诊所如何处理后需要修改
        List<Employee> employees = employeeService.findSimpleList(
                new LambdaQueryWrapper<Employee>()
                        .eq(query.getJob() != null, Employee::getJob, query.getJob())
                        .eq(query.getEmployeeId() != null, Employee::getId, query.getEmployeeId())
                        .like(Employee::getWorkHospitalIds, MultiIdUtil.getLikeSql(query.getHospitalId()))
        );

        if (employees.size() > 0) {
            List<ScheduleVO> schedules = getSqlMapper().findSchedule(query);
            List<ScheduleVO> fixedSchedules = getSqlMapper().findFixedSchedule(query);
            return new MonthScheduleVO(employees, schedules, fixedSchedules);
        }

        // 返回空
        return new MonthScheduleVO();
    }


    /**
     * 获取固定排班信息
     *
     * @param query
     * @return
     */
    public FixedScheduleVO getFixedSchedule(ScheduleQuery query) {
        // TODO 考虑workScope情况，在确认多诊所如何处理后需要修改
        List<Employee> employees = employeeService.findSimpleList(
                new LambdaQueryWrapper<Employee>()
                        .eq(query.getJob() != null, Employee::getJob, query.getJob())
                        .like(Employee::getWorkHospitalIds, MultiIdUtil.getLikeSql(query.getHospitalId()))
        );

        if (employees.size() > 0) {
            List<ScheduleVO> fixedSchedules = getSqlMapper().findFixedSchedule(query);
            return new FixedScheduleVO(employees, fixedSchedules);
        }

        // 返回空
        return new FixedScheduleVO();
    }


    /**
     * 新增或更新排班
     *
     * @param scheduleDTO
     */
    public void saveOrUpdateSchedule(ScheduleDTO scheduleDTO) {
        // TODO 增加是否日期为过去的判断

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
        Schedule schedule = new Schedule();
        SimpleBeanCopyUtil.simpleCopy(scheduleDTO, schedule);

        LocalDate localDate = TimeUtil.toLocalDate(schedule.getScheduleDate());
        schedule.setWeekday(localDate.getDayOfWeek().getValue());
        schedule.setWeek(localDate.get(WeekFields.ISO.weekOfWeekBasedYear()));
        schedule.setMonth(getMonth(localDate));
        schedule.setDay(localDate.getDayOfMonth());

        // TODO 考虑实际情况，根据更新还是新增多来处理下面逻辑

        int effect = getSqlMapper().update(schedule, new LambdaQueryWrapper<Schedule>()
                .eq(Schedule::getEmployeeId, scheduleDTO.getEmployeeId())
                .eq(Schedule::getHospitalId, scheduleDTO.getHospitalId())
                .eq(Schedule::getScheduleDate, localDate)
        );

        if (effect <= 0) {
            save(schedule);
        }
    }

    @Transactional
    public void saveOrUpdateSchedule(List<ScheduleDTO> dataList) {
        for (ScheduleDTO scheduleDTO : dataList) {
            // TODO 多班次录入可优化，通过查询已经存在的班次区分更新和新增，然后批量更新和新增
            saveOrUpdateSchedule(scheduleDTO);
        }
    }

    private int getMonth(LocalDate localDate) {
        return localDate.getYear() * 100 + localDate.getMonth().getValue();
    }
}