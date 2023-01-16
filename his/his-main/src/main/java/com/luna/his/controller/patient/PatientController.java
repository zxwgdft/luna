package com.luna.his.controller.patient;

import com.luna.framework.service.ControllerSupport;
import com.luna.his.patient.service.PatientService;
import com.luna.his.patient.service.dto.PatientFullDTO;
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

    @ApiOperation("新增患者")
    @PostMapping("/add")
    public void addPatient(@RequestBody @Valid PatientFullDTO dto, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        patientService.addPatient(dto);
    }


}
