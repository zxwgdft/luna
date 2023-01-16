package com.luna.his.sys.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "code")
@ApiModel(description = "资源权限")
public class SysPermission {

    @TableId
    @ApiModelProperty("权限code")
    private String code;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty("父权限ID")
    private String parentCode;

    @ApiModelProperty("列表顺序")
    private Integer orderNo;

    @ApiModelProperty("是否系统管理员权限")
    private Boolean isAdmin;

    @ApiModelProperty("是否可授权")
    private Boolean isGrant;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;
}