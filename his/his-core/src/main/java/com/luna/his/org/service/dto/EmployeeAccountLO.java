package com.luna.his.org.service.dto;

import com.luna.his.core.log.FieldType;
import com.luna.his.core.log.LogField;
import lombok.Data;

/**
 * @author TontoZhou
 */
@Data
public class EmployeeAccountLO {

    @LogField("账号")
    private String account;
    @LogField("手机号码")
    private String cellphone;
    @LogField(name = "角色", fieldType = FieldType.ROLE, isMulti = true)
    private String roleIds;

}
