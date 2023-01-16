package com.luna.tenant.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id")
@ApiModel(description = "账号")
@TableName("account")
public class Account {

    /**
     * 启用状态
     */
    public final static int STATE_ENABLED = 1;
    /**
     * 停用状态
     */
    public final static int STATE_DISABLED = 0;


    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("盐")
    private String salt;
    @ApiModelProperty("手机号")
    private String cellphone;
    @ApiModelProperty("关联人员ID")
    private Long userId;
    @ApiModelProperty("账号类型")
    private Integer type;
    @ApiModelProperty("应用服务名称")
    private String server;
    @ApiModelProperty("状态")
    private Integer state;
    @ApiModelProperty("最近一次登录时间")
    private Date lastLoginTime;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("租户ID")
    private Long tenantId;

}
