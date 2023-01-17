package com.luna.his.controller.core;

import com.luna.framework.api.PageResult;
import com.luna.framework.service.ControllerSupport;
import com.luna.his.patient.service.entity.EsPatient;
import com.luna.his.core.service.entity.LogLogin;
import com.luna.his.core.service.entity.LogOperate;
import com.luna.his.core.service.LogSearchService;
import com.luna.his.patient.service.PatientSearchService;
import com.luna.his.core.service.dto.LogLoginQuery;
import com.luna.his.core.service.dto.LogOperateQuery;
import com.luna.his.patient.service.dto.PatientQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TontoZhou
 */
@Api(tags = "搜索管理")
@RestController
@RequestMapping("/his/log")
@RequiredArgsConstructor
public class LogController {

    private final LogSearchService logSearchService;

    @ApiOperation("搜索用户登录日志")
    @PostMapping("/login/page")
    public PageResult<LogLogin> searchLogLogin(@RequestBody LogLoginQuery query) {
        return logSearchService.searchLoginLog(query);
    }

    @ApiOperation("搜索用户操作日志")
    @PostMapping("/operate/page")
    public PageResult<LogOperate> searchLogOperate(@RequestBody LogOperateQuery query) {
        return logSearchService.searchOperateLog(query);
    }
}