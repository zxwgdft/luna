package com.luna.tenant.controller;

import com.luna.framework.service.ControllerSupport;
import com.luna.tenant.model.Server;
import com.luna.tenant.service.ServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TontoZhou
 */
@Api(tags = "服务器管理")
@RestController
@RequestMapping("/tenant/server")
@RequiredArgsConstructor
public class ServerController extends ControllerSupport {

    private final ServerService serverService;

    @ApiOperation("服务列表")
    @GetMapping("/list")
    public List<Server> getServerList() {
        return serverService.findList();
    }


}