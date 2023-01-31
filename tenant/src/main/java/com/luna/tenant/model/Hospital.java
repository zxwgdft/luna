package com.luna.tenant.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.tenant.core.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "租户诊所")
@TableName("tenant_hospital")
public class Hospital extends BaseModel {

    public static final int STATE_INITIALIZING = 1; // 初始化中
    public static final int STATE_CREATE_FAIL = 8; // 创建失败，初始化失败
    public static final int STATE_CREATE_SUCCESS = 9; // 创建成功

    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("诊所名称")
    private String name;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("是否总部")
    private Boolean isHeadquarter;

    @ApiModelProperty("开始营业时间")
    private Integer openTime;

    @ApiModelProperty("停止营业时间")
    private Integer closeTime;

    @ApiModelProperty("休息日")
    private String restWeekdays;

    @ApiModelProperty("状态")
    private Integer state;

}