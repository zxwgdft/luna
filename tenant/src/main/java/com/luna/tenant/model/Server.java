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
@EqualsAndHashCode(of = "name")
@ApiModel(description = "服务器")
@TableName("server")
public class Server extends BaseModel {

    @ApiModelProperty("服务器ID")
    @TableId(type = IdType.NONE)
    private String id;

    @ApiModelProperty("服务器名称")
    private String name;

    @ApiModelProperty("服务器备注说明")
    private String note;

}