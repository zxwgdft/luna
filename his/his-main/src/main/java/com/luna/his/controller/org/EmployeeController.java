package com.luna.his.controller.org;

import com.luna.framework.api.PageResult;
import com.luna.framework.service.ControllerSupport;
import com.luna.his.controller.org.vo.Data4EmployeeVO;
import com.luna.his.org.model.Employee;
import com.luna.his.org.service.EmployeeService;
import com.luna.his.org.service.HospitalService;
import com.luna.his.org.service.dto.EmployeeAccountDTO;
import com.luna.his.org.service.dto.EmployeeDTO;
import com.luna.his.org.service.dto.EmployeeQuery;
import com.luna.his.org.service.OrgRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author : TontoZhou
 */
@Api(tags = "员工管理")
@RestController
@RequestMapping("/his/org/employee")
@RequiredArgsConstructor
public class EmployeeController extends ControllerSupport {

    private final EmployeeService employeeService;
    private final HospitalService hospitalService;
    private final OrgRoleService roleService;

    @ApiOperation("查询员工列表（简单信息）")
    @PostMapping("/find/list/simple")
    public List<Employee> findSimpleEmployeeList(@RequestBody EmployeeQuery employeeQuery) {
        return employeeService.findSimpleEmployeeList(employeeQuery);
    }

    @ApiOperation("查询员工列表(详细信息)")
    @PostMapping("/find/list")
    public List<Employee> findEmployeeList(@RequestBody EmployeeQuery employeeQuery) {
        return employeeService.findEmployeeList(employeeQuery);
    }

    @ApiOperation("查询员工列表（分页）")
    @PostMapping("/find/page")
    public PageResult<Employee> findEmployeePage(@RequestBody EmployeeQuery employeeQuery) {
        return employeeService.findEmployeePage(employeeQuery);
    }

    @ApiOperation("查询员工列表（分页）")
    @GetMapping("/get")
    public Employee getEmployee(@RequestParam Long id) {
        return employeeService.getEmployee(id);
    }

    @ApiOperation("获取员工基础数据")
    @PostMapping("/get/data4Employee")
    public Data4EmployeeVO getData4Employee() {
        return new Data4EmployeeVO(roleService.findSimpleList(), hospitalService.findSimpleList());
    }

    @ApiOperation("新增员工")
    @PostMapping("/add")
    public void save(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        employeeService.addEmployee(employeeDTO);
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public void update(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        employeeService.updateEmployee(employeeDTO);
    }

    @ApiOperation("修改员工账号")
    @PostMapping("/account/update")
    public void update(@Valid @RequestBody EmployeeAccountDTO accountDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        employeeService.createOrUpdateEmployeeAccount(accountDTO);
    }


}
