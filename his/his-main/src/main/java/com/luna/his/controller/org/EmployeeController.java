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
import com.luna.his.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmployeeController extends ControllerSupport {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private DepartmentService departmentService;

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
    @GetMapping("/get/detail")
    public EmployeeDetailVO getEmployeeDetail(@RequestParam Long id) {
        return employeeService.getEmployeeDetail(id);
    }

    @ApiOperation("获取员工基础数据")
    @PostMapping("/get/data4Employee")
    public Data4EmployeeVO getData4Employee() {
        return new Data4EmployeeVO(roleService.findSimpleList(), departmentService.findSimpleList(), hospitalService.findSimpleList());
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

    @ApiOperation("根据工作科室查询员工列表（查询不在该科室下的所有员工）")
    @PostMapping("/find/findEmployeeByWorkingDepartId")
    public PageResult<Employee> findEmployeeByWorkingDepartId(@RequestBody EmployeeQuery employeeQuery) {
        return employeeService.findEmployeeByWorkingDepartId(employeeQuery);
    }

    @ApiOperation("修改工作科室")
    @PostMapping("/updateWorkDepartIds")
    public boolean updateWorkDepartIds(@Valid @RequestBody EmployeeDeptDTO employeeDeptDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        return employeeService.updateWorkDepartIds(employeeDeptDTO);
    }

    @ApiOperation("移除工作科室")
    @PostMapping("/removeFromWorkDepartIds")
    public boolean removeFromWorkDepartIds(@RequestParam Long id, @RequestParam Long workingDepartId) {
        return employeeService.removeFromWorkDepartIds(id, workingDepartId);
    }


}
