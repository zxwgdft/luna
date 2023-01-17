package com.luna.his.core.es;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 时间范围
 * <p>
 * 如果指定了是日期则将自动处理边界问题，例如传值为2022-11-01 12:00:00到2022-11-02 13:00:00，
 * 将处理为  >=2022-11-01 00:00:00 AND <2022-11-03 00:00:00
 * <p>
 * 非指明日期情况，则按照 >=start AND <= end
 *
 * @author TontoZhou
 */
@Data
public final class DateRange {

    @ApiModelProperty("是否日期（不含时分秒），如果是日期后端会自动处理结束日期的边界问题，最终结果将包含结束日期那天的数据")
    private boolean isDate;

    @ApiModelProperty("开始时间")
    private Date start;
    @ApiModelProperty("结束时间")
    private Date end;

}
