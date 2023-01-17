package com.luna.his.patient.service;

import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.core.HisUserSession;
import com.luna.his.core.framework.HisServiceSupport;
import com.luna.his.core.log.*;
import com.luna.his.org.service.EmployeeService;
import com.luna.his.patient.mapper.PatientMapper;
import com.luna.his.patient.model.Patient;
import com.luna.his.patient.model.PatientContact;
import com.luna.his.patient.model.PatientRelation;
import com.luna.his.patient.model.PatientSelf;
import com.luna.his.patient.service.dto.PatientFullDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 患者相关服务
 */
@Service
@RequiredArgsConstructor
public class PatientService extends HisServiceSupport<Patient, PatientMapper> {

    private final PatientContactService contactService;
    private final PatientRelationService relationService;
    private final PatientSelfService selfService;
    private final EmployeeService employeeService;
    private final PatientSearchService patientSearchService;


    /**
     * 新增病人
     *
     * @param patientDTO
     */
    @Transactional
    @ActionLog(module = Module.PATIENT_MANAGE, actionType = ActionType.ADD, action = "新增患者", hasContent = true)
    public void addPatient(PatientFullDTO patientDTO) {
        HisUserSession currentUserSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
        long hospitalId = currentUserSession.getHospitalId();

        // TODO 待重做
        long patientId = System.currentTimeMillis();

        // 保存病人信息
        Patient patient = new Patient();
        SimpleBeanCopyUtil.simpleCopy(patientDTO, patient);
        patient.setId(patientId);
        patient.setCreateHospitalId(hospitalId);
        patient.setCreateById(currentUserSession.getUserId());
        save(patient);

        patientDTO.setId(patientId);
        patientDTO.setCreateTime(new Date());
        patientDTO.setTenantId(patient.getTenantId());
        patientDTO.setCreateHospitalId(patient.getCreateHospitalId());
        patientDTO.setCreateById(patient.getCreateById());

        // 保存病人个人信息
        PatientSelf patientSelf = new PatientSelf();
        SimpleBeanCopyUtil.simpleCopy(patientDTO, patientSelf);
        selfService.save(patientSelf);

        // 保存病人联系方式
        PatientContact patientContact = new PatientContact();
        SimpleBeanCopyUtil.simpleCopy(patientDTO, patientContact);
        contactService.save(patientContact);

        // 保存病人关系信息
        PatientRelation patientRelation = new PatientRelation();
        SimpleBeanCopyUtil.simpleCopy(patientDTO, patientRelation);
        relationService.save(patientRelation);

        patientSearchService.savePatient(patientDTO);

        // 设置更新内容到操作日志
        OperateLogHelper.setPatient(patientId);
        OperateLogHelper.setDataUpdate(new DataUpdate(null, patient, null));
        OperateLogHelper.setDataUpdate(new DataUpdate(null, patientSelf, null));
        OperateLogHelper.setDataUpdate(new DataUpdate(null, patientContact, null));
        OperateLogHelper.setDataUpdate(new DataUpdate(null, patientRelation, null));
    }


}