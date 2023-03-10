package com.luna.framework.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SortParam {

    @ApiModelProperty("排序字段")
    private String sort;
    @ApiModelProperty("排序方式")
    private String order;

}
