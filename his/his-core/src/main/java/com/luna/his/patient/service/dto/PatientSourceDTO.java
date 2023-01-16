package com.luna.his.patient.service.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.luna.his.core.log.FieldType;
import com.luna.his.core.log.LogField;
import com.luna.his.core.log.converter.PatientSourceConverter;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PatientSourceDTO {

    @ApiModelProperty("病人ID")
    @NotNull(message = "病人ID不能为空")
    @LogField(name = "患者", fieldType = FieldType.PATIENT)
    private Long id;

    @ApiModelProperty("病人ID集合")
    @Size(min = 1, message = "病人ID集合不能为空")
    private List<Long> ids;

    @ApiModelProperty("来源类型（1：外部来源，2：朋友介绍，3：员工介绍）")
    @NotNull(message = "病人ID不能为空")
    @LogField(name = "患者来源", converter = PatientSourceConverter.class, isFilterEqual = false)
    private Integer sourceType;

    @ApiModelProperty("一级来源")
    private Integer srcOtherLevel1;

    @ApiModelProperty("二级来源")
    private Integer srcOtherLevel2;

    @ApiModelProperty("三级来源")
    private Integer srcOtherLevel3;

    @ApiModelProperty("员工介绍人ID")
    private Long srcEmployeeId;

    @ApiModelProperty("患者介绍人ID")
    private Long srcPatientId;
}
