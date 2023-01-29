package com.luna.his.org.service.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
public class RoleDTO {

    private Long id;

    @NotEmpty(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50")
    private String name;

}