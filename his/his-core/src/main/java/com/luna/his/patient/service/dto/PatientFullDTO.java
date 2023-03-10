package com.luna.his.patient.service.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
@ApiModel(description = "新增患者信息")
public class PatientFullDTO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("患者类型")
    private String type;

    @ApiModelProperty("所属医院ID")
    private Long hid;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("姓名（拼音首字母）")
    private String namePy;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("来源类型")
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

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建医院ID")
    private Long createHospitalId;

    @ApiModelProperty("创建人ID")
    private Long createById;

    @ApiModelProperty("租户ID")
    private Long tenantId;


    // ------ 个人信息 ---------

    @ApiModelProperty("生日")
    private Date birthday;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("国籍")
    private String nationality;

    @ApiModelProperty("身份证")
    private String idCardNo;

    @ApiModelProperty("发证机关")
    private String idCardIssuer;

    @ApiModelProperty("有效期")
    private Date expireDate;

    @ApiModelProperty("血型")
    private String bloodType;

    @ApiModelProperty("籍贯")
    private String nativePlace;

    @ApiModelProperty("婚姻")
    private String marriage;

    @ApiModelProperty("职业")
    private String occupation;

    @ApiModelProperty("宗教")
    private String religion;

    @ApiModelProperty("监护人")
    private String guardian;

    @ApiModelProperty("其他证件类型")
    private Integer otherIdType;

    @ApiModelProperty("其他证件号码")
    private String otherIdNo;

    @ApiModelProperty("文化程度")
    private Integer educationLevel;

    // ------ 联系方式 ---------

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("微信")
    private String weixin;

    @ApiModelProperty("QQ")
    private String qq;

    @ApiModelProperty("手机1")
    private String mobile1;

    @ApiModelProperty("手机1属性")
    private String mobile1re;

    @ApiModelProperty("手机2")
    private String mobile2;

    @ApiModelProperty("手机2属性")
    private String mobile2re;

    @ApiModelProperty("手机3")
    private String mobile3;

    @ApiModelProperty("手机3属性")
    private String mobile3re;

    @ApiModelProperty("电话1")
    private String phone1;

    @ApiModelProperty("电话1属性")
    private String phone1re;

    @ApiModelProperty("电话2")
    private String phone2;

    @ApiModelProperty("电话2属性")
    private String phone2re;

    @ApiModelProperty("省")
    private Long province;

    @ApiModelProperty("市")
    private Long city;

    @ApiModelProperty("区")
    private Long district;

    @ApiModelProperty("地址")
    private String address;

    // ------ 客户关系 ---------

    @ApiModelProperty("责任医生ID")
    private Long doctorId;

    @ApiModelProperty("开发人员ID")
    private Long developerId;

    @ApiModelProperty("咨询师ID")
    private Long consultantId;

    @ApiModelProperty("网电咨询师ID")
    private Long onlineConsultantId;

    @ApiModelProperty("客服ID")
    private Long attendantId;

    @ApiModelProperty("顾问ID")
    private Long advisorId;

}
