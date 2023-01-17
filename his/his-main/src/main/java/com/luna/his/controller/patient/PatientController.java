package com.luna.his.controller.patient;

import com.luna.framework.api.PageResult;
import com.luna.framework.service.ControllerSupport;
import com.luna.his.patient.service.PatientSearchService;
import com.luna.his.patient.service.PatientService;
import com.luna.his.patient.service.dto.PatientFullDTO;
import com.luna.his.patient.service.dto.PatientQuery;
import com.luna.his.patient.service.entity.EsPatient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "患者中心-患者信息管理")
@RestController
@RequestMapping("/his/patient")
@RequiredArgsConstructor
public class PatientController extends ControllerSupport {

    private final PatientService patientService;
    private final PatientSearchService patientSearchService;

    @ApiOperation("搜索患者")
    @PostMapping("/find/page")
    public PageResult<EsPatient> searchPatient(@RequestBody PatientQuery query) {
        return patientSearchService.searchPatient(query);
    }

    @ApiOperation("新增患者")
    @PostMapping("/add")
    public void addPatient(@RequestBody @Valid PatientFullDTO dto, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        patientService.addPatient(dto);
    }


}
