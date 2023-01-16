package com.luna.his.patient.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.his.core.BaseModel;

import com.luna.his.core.log.FieldType;
import com.luna.his.core.log.LogField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "病人客户关系")
@TableName("patient_relation")
public class PatientRelation extends BaseModel {

    @ApiModelProperty("病人ID")
    @TableId
    private Long id;

    @ApiModelProperty("责任医生ID")
    @LogField(name = "责任医生", fieldType = FieldType.EMPLOYEE)
    private Long doctorId;

    @ApiModelProperty("开发人员ID")
    @LogField(name = "开发人员", fieldType = FieldType.EMPLOYEE)
    private Long developerId;

    @ApiModelProperty("咨询师ID")
    @LogField(name = "咨询师", fieldType = FieldType.EMPLOYEE)
    private Long consultantId;

    @ApiModelProperty("网电咨询师ID")
    @LogField(name = "网电咨询师", fieldType = FieldType.EMPLOYEE)
    private Long onlineConsultantId;

    @ApiModelProperty("客服ID")
    @LogField(name = "客服", fieldType = FieldType.EMPLOYEE)
    private Long attendantId;

    @ApiModelProperty("顾问ID")
    @LogField(name = "顾问", fieldType = FieldType.EMPLOYEE)
    private Long advisorId;


}