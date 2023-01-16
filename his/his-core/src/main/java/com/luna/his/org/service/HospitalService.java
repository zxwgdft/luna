package com.luna.his.org.service;

import com.luna.framework.exception.BusinessException;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.core.framework.HisServiceSupport;
import com.luna.his.org.mapper.HospitalMapper;
import com.luna.his.org.model.Hospital;
import com.luna.his.org.service.dto.HospitalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HospitalService extends HisServiceSupport<Hospital, HospitalMapper> {


    public boolean updateHospital(HospitalDTO hospitalDTO) {
        Long id = hospitalDTO.getId();
        if(id == null) {
            throw new BusinessException("找不到更新的医院");
        }
        Hospital model = get(id);

        SimpleBeanCopyUtil.simpleCopy(hospitalDTO, model);
        updateSelection(model);
        return true;
    }
}