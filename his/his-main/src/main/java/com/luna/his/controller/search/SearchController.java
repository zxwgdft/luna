package com.luna.his.controller.search;

import com.luna.framework.api.PageResult;
import com.luna.framework.service.ControllerSupport;
import com.luna.his.search.entity.EsPatient;
import com.luna.his.search.entity.LogLogin;
import com.luna.his.search.entity.LogOperate;
import com.luna.his.search.service.LogSearchService;
import com.luna.his.search.service.PatientSearchService;
import com.luna.his.search.service.query.LogLoginQuery;
import com.luna.his.search.service.query.LogOperateQuery;
import com.luna.his.search.service.query.PatientGroupQuery;
import com.luna.his.search.service.query.PatientQuery;
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
@RequestMapping("/his/search")
@RequiredArgsConstructor
public class SearchController extends ControllerSupport {

    private final PatientSearchService patientSearchService;
    private final LogSearchService logSearchService;

    @ApiOperation("搜索患者")
    @PostMapping("/patient/page")
    public PageResult<EsPatient> searchPatient(@RequestBody PatientQuery query) {
        return patientSearchService.searchPatient(query);
    }

    @ApiOperation("搜索手动分组患者")
    @PostMapping("/patient/group/page")
    public PageResult<EsPatient> searchGroupPatient(@RequestBody PatientGroupQuery query) {
        return patientSearchService.searchGroupPatient(query);
    }

    @ApiOperation("搜索用户登录日志")
    @PostMapping("/log/login/page")
    public PageResult<LogLogin> searchLogLogin(@RequestBody LogLoginQuery query) {
        return logSearchService.searchLoginLog(query);
    }

    @ApiOperation("搜索用户操作日志")
    @PostMapping("/log/operate/page")
    public PageResult<LogOperate> searchLogOperate(@RequestBody LogOperateQuery query) {
        return logSearchService.searchOperateLog(query);
    }
}