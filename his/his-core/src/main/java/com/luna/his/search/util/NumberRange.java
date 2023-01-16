package com.luna.his.search.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
public final class NumberRange {

    @ApiModelProperty("是否包含下边界")
    private boolean includeLower;
    @ApiModelProperty("是否包含上边界")
    private boolean includeUpper;

    private String start;
    private String end;
}
