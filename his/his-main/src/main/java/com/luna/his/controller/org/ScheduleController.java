package com.luna.his.controller.org;

import com.luna.framework.service.ControllerSupport;
import com.luna.his.org.service.ScheduleFixedService;
import com.luna.his.org.service.ScheduleService;
import com.luna.his.org.service.dto.ScheduleDTO;
import com.luna.his.org.service.dto.ScheduleFixedDTO;
import com.luna.his.org.service.dto.ScheduleQuery;
import com.luna.his.org.service.vo.FixedScheduleVO;
import com.luna.his.org.service.vo.MonthScheduleVO;
import com.luna.his.org.service.vo.WeekScheduleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author : TontoZhou
 */
@Api(tags = "排班管理")
@RestController
@RequestMapping("/his/schedule")
@RequiredArgsConstructor
public class ScheduleController extends ControllerSupport {

    private final ScheduleService scheduleService;
    private final ScheduleFixedService scheduleFixedService;

    @ApiOperation("获取周排班")
    @PostMapping("/get/week")
    public WeekScheduleVO findWeekSchedule(@RequestBody ScheduleQuery query) {
        return scheduleService.getWeekSchedule(query);
    }

    @ApiOperation("获取月排班")
    @PostMapping("/get/month")
    public MonthScheduleVO findMonthSchedule(@RequestBody ScheduleQuery query) {
        return scheduleService.getMonthSchedule(query);
    }

    @ApiOperation("获取固定排班")
    @PostMapping("/get/fixed")
    public FixedScheduleVO getFixedSchedule(@RequestBody ScheduleQuery query) {
        return scheduleService.getFixedSchedule(query);
    }

    @ApiOperation("设置排班")
    @PostMapping("/set")
    public void setSchedule(@RequestBody @Validated ScheduleDTO scheduleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        scheduleService.saveOrUpdateSchedule(scheduleDTO);
    }

    @ApiOperation("设置排班（多个）")
    @PostMapping("/set/multi")
    public void setMultiSchedule(@RequestBody @Validated List<ScheduleDTO> dataList, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        scheduleService.saveOrUpdateSchedule(dataList);
    }

    @ApiOperation("设置固定排班")
    @PostMapping("/set/fixed")
    public void setFixedSchedule(@RequestBody @Validated ScheduleFixedDTO scheduleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        scheduleFixedService.saveOrUpdateSchedule(scheduleDTO);
    }
}
