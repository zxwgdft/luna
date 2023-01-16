package com.luna.framework.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "分页返回结果")
public class PageResult<T> {

    @ApiModelProperty("页码")
    private int page;

    @ApiModelProperty("每页大小")
    private int limit;

    @ApiModelProperty("总数据量")
    private long total;

    @ApiModelProperty("数据")
    private List<T> data;


    public PageResult() {

    }

    public PageResult(int page, int limit, long total, List<T> data) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.data = data;
    }

}
