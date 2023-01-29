package com.luna.tenant.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "luna-tenant", contextId = "tenant")
public interface TenantClient {

    @ApiOperation(value = "初始化诊所数据成功处理")
    @PostMapping(value = "/internal/tenant/hospital/init/success")
    void createHospitalSuccessHandler(@RequestParam Long hospitalId);

    @ApiOperation(value = "初始化诊所数据失败处理")
    @PostMapping(value = "/internal/tenant/hospital/init/fail")
    void createHospitalFailHandler(@RequestParam Long hospitalId);
}
