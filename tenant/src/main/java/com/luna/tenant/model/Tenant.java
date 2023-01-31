package com.luna.tenant.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luna.tenant.core.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "租户")
@TableName("tenant")
public class Tenant extends BaseModel {

    public final static int STATE_ENABLED = 1; // 租户可用
    public final static int STATE_EXPIRED = 2;    // 租户过期
    public final static int STATE_LOCKED = 3;    // 租户被锁

    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("租户名称")
    private String name;

    @ApiModelProperty("服务器")
    private String server;

    @ApiModelProperty("有效期")
    private Date expireDate;

    @ApiModelProperty("总部关联诊所ID")
    private Long headquarterId;

    @ApiModelProperty("是否可用")
    private Boolean isEnabled;

    @ApiModelProperty("是否被锁")
    private Boolean isLocked;

    @ApiModelProperty("状态")
    private Integer state;
}