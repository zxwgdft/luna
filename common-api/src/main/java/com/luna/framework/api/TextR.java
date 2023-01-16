package com.luna.framework.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2021/6/25
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel(description = "文本返回")
public class TextR {

    @ApiModelProperty("文本")
    private String text;
}
