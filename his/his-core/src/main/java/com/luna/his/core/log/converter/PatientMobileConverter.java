package com.luna.his.core.log.converter;

import com.luna.his.core.log.FieldValueConverter;
import com.luna.his.core.log.LogField;
import com.luna.his.core.mapper.LogOperateMapper;
import com.luna.his.patient.model.PatientContact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author TontoZhou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PatientMobileConverter implements FieldValueConverter {

    private final LogOperateMapper logOperateMapper;

    @Override
    public Object convert(Object fieldVal, Object obj, LogField logField) {
        PatientContact contact = (PatientContact) obj;

        StringBuilder sb = new StringBuilder();

        format(contact.getMobile1re(), contact.getMobile1(), sb);
        format(contact.getMobile2re(), contact.getMobile2(), sb);
        format(contact.getMobile3re(), contact.getMobile3(), sb);

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }

        return null;
    }

    private void format(String re, String phone, StringBuilder sb) {
        if (re != null && phone != null) {
            sb.append(re).append(':').append(phone).append("，");
        }
    }
}
