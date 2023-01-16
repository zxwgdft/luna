package com.luna.framework.api;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 逻辑删除基础模型类
 * @author TontoZhou
 */
@Getter
@Setter
public class LogicDeleteModel implements Serializable {

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    @TableLogic
    private Boolean isDeleted;

}
