package com.luna.tenant.core;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础模型类
 *
 * @author TontoZhou
 */
@Getter
@Setter
public class BaseModel implements Serializable {

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

}
