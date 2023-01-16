package com.luna.his.core.log.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.luna.his.core.constant.PatientSource;
import com.luna.his.core.log.FieldValueConverter;
import com.luna.his.core.log.LogField;
import com.luna.his.core.mapper.LogOperateMapper;
import org.springframework.stereotype.Component;

import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.core.log.FixedTypeConverter;
import com.luna.his.patient.model.Patient;
import com.luna.his.patient.service.dto.PatientSourceDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TontoZhou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PatientSourceConverter implements FieldValueConverter {

    private final LogOperateMapper logOperateMapper;

    @Override
    public Object convert(Object fieldVal, Object obj, LogField logField) {
        if (fieldVal != null) {
            int sourceType = (int) fieldVal;
            Patient patient = new Patient();
            if (obj instanceof Patient) {
                patient = (Patient) obj;
            } else if (obj instanceof PatientSourceDTO) {
                PatientSourceDTO sourceDTO = (PatientSourceDTO) obj;
                SimpleBeanCopyUtil.simpleCopy(sourceDTO, patient);
            }
            if (sourceType == PatientSource.FRIEND.getCode()) {
                String patientName = logOperateMapper.getPatientName(patient.getSrcPatientId());
                return "朋友介绍-" + Optional.ofNullable(patientName).orElse("未知");
            } else if (sourceType == PatientSource.EMPLOYEE.getCode()) {
                String employeeName = logOperateMapper.getEmployeeName(patient.getSrcEmployeeId());
                return "员工介绍-" + Optional.ofNullable(employeeName).orElse("未知");
            } else if (sourceType == PatientSource.EXTERNAL.getCode()) {
                List<Integer> ids = new ArrayList<>(3);
                if (patient.getSrcOtherLevel1() != null) {
                    ids.add(patient.getSrcOtherLevel1());
                    if (patient.getSrcOtherLevel2() != null) {
                        ids.add(patient.getSrcOtherLevel2());
                        if (patient.getSrcOtherLevel3() != null) {
                            ids.add(patient.getSrcOtherLevel3());
                        }
                    }
                }

                if (ids.size() > 0) {
                    List<FixedTypeConverter.IdName> idNames = logOperateMapper.findPatientSource(ids);
                    StringBuilder sb = new StringBuilder();
                    for (int id : ids) {
                        for (FixedTypeConverter.IdName idName : idNames) {
                            if (idName.getId().intValue() == id) {
                                sb.append(idName.getName()).append('-');
                                break;
                            }
                        }
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                        return sb.toString();
                    }
                }
            }
        }
        return null;
    }

}
