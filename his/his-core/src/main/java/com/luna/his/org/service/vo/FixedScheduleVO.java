package com.luna.his.org.service.vo;

import com.luna.his.org.model.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TontoZhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "固定排班数据")
public class FixedScheduleVO {

    @ApiModelProperty("员工")
    private List<Employee> employees;

    @ApiModelProperty("固定排班")
    private List<ScheduleVO> fixedSchedules;

}
