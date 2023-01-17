package com.luna.his.controller.org;

import com.luna.framework.api.PageResult;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.service.ControllerSupport;
import com.luna.his.core.HisUserSession;
import com.luna.his.org.model.Hospital;
import com.luna.his.org.service.HospitalService;
import com.luna.his.org.service.dto.HospitalDTO;
import com.luna.his.org.service.dto.HospitalQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author : TontoZhou
 */
@Api(tags = "医院管理")
@RestController
@RequestMapping("/his/org/hospital")
@RequiredArgsConstructor
public class HospitalController extends ControllerSupport {

    private final HospitalService hospitalService;

    @PostMapping("/find/page")
    @ApiOperation("查询医院分页列表")
    public PageResult<Hospital> findPage(@RequestBody HospitalQuery query) {
        return hospitalService.findPage(query);
    }

    @PostMapping("/find/list/simple")
    @ApiOperation("查询医院列表（简单信息）")
    public List<Hospital> findSimpleList(@RequestBody HospitalQuery query) {
        return hospitalService.findSimpleList(query);
    }

    @PostMapping("/get/current")
    @ApiOperation("获取当前工作医院信息")
    public Hospital getCurrentHospital() {
        HisUserSession currentUserSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
        return hospitalService.get(currentUserSession.getHospitalId());
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public boolean update(@Valid @RequestBody HospitalDTO hospitalDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        return hospitalService.updateHospital(hospitalDTO);
    }
}
