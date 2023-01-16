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

    public final static int STATE_NONE = 0;    // 无状态
    public final static int STATE_CREATING = 1;    // 正在创建
    public final static int STATE_ENABLED = 8; // 租户可用
    public final static int STATE_DISABLED = 9;    // 租户不可用

    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("租户名称")
    private String name;

    @ApiModelProperty("服务器ID")
    private Long serverId;

    @ApiModelProperty("有效期")
    private Date expireDate;

    @ApiModelProperty("状态")
    private Integer state;
}