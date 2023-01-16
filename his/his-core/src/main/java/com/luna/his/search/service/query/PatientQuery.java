package com.luna.his.search.service.query;

import com.luna.framework.api.PageParam;
import com.luna.his.search.util.DateRange;
import com.luna.his.search.util.ESQuery;
import com.luna.his.search.util.ESQueryType;
import com.luna.his.search.util.NumberRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "患者查询条件")
public class PatientQuery extends PageParam {

    @ApiModelProperty("搜索关键字")
    private String keyword;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("病案号")
    @ESQuery(type = ESQueryType.LIKE)
    private Long docNo;

    @ApiModelProperty("患者类型")
    @ESQuery(type = ESQueryType.EQUAL)
    private String type;

    @ApiModelProperty("性别")
    @ESQuery(type = ESQueryType.EQUAL)
    private String sex;

    @ApiModelProperty("来源类型")
    @ESQuery(type = ESQueryType.EQUAL)
    private Integer sourceType;

    @ApiModelProperty("一级来源")
    @ESQuery(type = ESQueryType.EQUAL)
    private Integer srcOtherLevel1;

    @ApiModelProperty("二级来源")
    @ESQuery(type = ESQueryType.EQUAL)
    private Integer srcOtherLevel2;

    @ApiModelProperty("三级来源")
    @ESQuery(type = ESQueryType.EQUAL)
    private Integer srcOtherLevel3;

    @ApiModelProperty("员工介绍人ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long srcEmployeeId;

    @ApiModelProperty("患者介绍人ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long srcPatientId;

    @ApiModelProperty("创建医院ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long createHospitalId;

    @ApiModelProperty("创建人ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private String createById;

    @ApiModelProperty("创建时间")
    @ESQuery(type = ESQueryType.BETWEEN)
    private DateRange createTime;

    @ApiModelProperty("租户ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long tenantId;

    // ------ 个人信息 ---------

    @ApiModelProperty("生日")
    @ESQuery(type = ESQueryType.BETWEEN)
    private DateRange birthday;

    @ApiModelProperty("国籍")
    @ESQuery(type = ESQueryType.EQUAL)
    private String nationality;

    @ApiModelProperty("职业")
    @ESQuery(type = ESQueryType.EQUAL)
    private String occupation;

    @ApiModelProperty("年龄")
    @ESQuery(type = ESQueryType.BETWEEN)
    private NumberRange age;

    // ------ 联系方式 ---------

    @ApiModelProperty("是否绑微信")
    private Boolean hasWeixin;

    @ApiModelProperty("手机号码")
    @ESQuery(type = ESQueryType.EQUAL)
    private String mobiles;

    @ApiModelProperty("电话号码")
    @ESQuery(type = ESQueryType.EQUAL)
    private String phones;

    @ApiModelProperty("省")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long province;

    @ApiModelProperty("市")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long city;

    @ApiModelProperty("区")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long district;

    @ApiModelProperty("地址")
    @ESQuery(type = ESQueryType.LIKE)
    private String address;

    // ------ 客户关系 ---------

    @ApiModelProperty("责任医生ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long doctorId;

    @ApiModelProperty("开发人员ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long developerId;

    @ApiModelProperty("咨询师ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long consultantId;

    @ApiModelProperty("网电咨询师ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long onlineConsultantId;

    @ApiModelProperty("客服ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long attendantId;

    @ApiModelProperty("顾问ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long advisorId;

    // ------ 病患信息 ---------

    @ApiModelProperty("患者标签")
    @ESQuery(type = ESQueryType.EQUAL)
    private String tags;

    @ApiModelProperty("患者备注")
    @ESQuery(type = ESQueryType.LIKE)
    private String remark;

    // ------ 初诊信息 ---------

    @ApiModelProperty("初诊时间")
    @ESQuery(type = ESQueryType.BETWEEN)
    private DateRange visitDate;

    @ApiModelProperty("初诊医生ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long visitDoctorId;

    @ApiModelProperty("上次就诊时间")
    @ESQuery(type = ESQueryType.BETWEEN)
    private DateRange lastVisitDate;

    @ApiModelProperty("上次就诊医生ID")
    @ESQuery(type = ESQueryType.EQUAL)
    private Long lastVisitDoctorId;

    // TODO 缺少欠费金额和会员类型

    // ------ 自由项 ---------

    @ApiModelProperty("自由项1")
    private String ext1;

    @ApiModelProperty("自由项2")
    private String ext2;

    @ApiModelProperty("自由项3")
    private String ext3;

    @ApiModelProperty("自由项4")
    private String ext4;

    @ApiModelProperty("自由项5")
    private String ext5;

    @ApiModelProperty("自由项6")
    private String ext6;

    @ApiModelProperty("自由项7")
    private String ext7;

    @ApiModelProperty("自由项8")
    private String ext8;

    @ApiModelProperty("自由项9")
    private String ext9;

    @ApiModelProperty("自由项10")
    private String ext10;

    @ApiModelProperty("自由项11")
    private String ext11;

    @ApiModelProperty("自由项12")
    private String ext12;

    @ApiModelProperty("自由项13")
    private String ext13;

    @ApiModelProperty("自由项14")
    private String ext14;

    @ApiModelProperty("自由项15")
    private String ext15;

    @ApiModelProperty("自由项16")
    private String ext16;

    @ApiModelProperty("自由项17")
    private String ext17;

    @ApiModelProperty("自由项18")
    private String ext18;

    @ApiModelProperty("自由项19")
    private String ext19;

    @ApiModelProperty("自由项20")
    private String ext20;

}