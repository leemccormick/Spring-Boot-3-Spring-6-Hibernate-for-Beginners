package com.leemccomick.springbooy.cruddemo.rest;

import com.leemccomick.springbooy.cruddemo.dao.EmployeeDAO;
import com.leemccomick.springbooy.cruddemo.entity.Employee;
import com.leemccomick.springbooy.cruddemo.service.EmployeeService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    /* This code before using Service Class
    private EmployeeDAO employeeDAO;

    // 1) Quick and dirty : Inject employee dao (user constructor injection)
    public EmployeeRestController(EmployeeDAO theEmployeeDAO) {
        employeeDAO = theEmployeeDAO;
    }
    */
    // 1) Inject employee dao, using Service Class
    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }

    // 2) HTTP Request / Endpoints
    // 2.1) Expose "/employees" and return a list of employee
    @GetMapping("/employees")
    public List<Employee> findAll() {
        // return employeeDAO.findAll(); --> This code before using Service Class
        return employeeService.findAll();
    }

    // 2.2) Add mapping for GET "/employees/{employeeId}" and return an employee
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);

        if (theEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        } else {
            return theEmployee;
        }
    }

    // 2.3) Add mapping for POST "/employees" to add new employee to database --> Using @PostMapping and @RequestBody
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee) {
        // 1) Just in case, they pass an id in JSON, set id to 0
        theEmployee.setId(0);

        // 2) Force a save of new item, instead of update
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

    // 2.4) Add mapping for PUT "/employees" to update employee in database --> Using @PutMapping and @RequestBody
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        // 1) Update given employee
        Employee dbEmployee = employeeService.save(theEmployee);
        // 2) Return updated employee
        return dbEmployee;
    }

    // 2.5) Add mapping for DELETE "/employees/{employeeId}" to delete employee in database --> Using @DeleteMapping and @PathVariable
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        // 1) Find the employee with id
        Employee tempEmployee = employeeService.findById(employeeId);

        // 2) If null throw exception or not null delete the employee
        if (tempEmployee == null) {
            throw new RuntimeException("Enployee id not found - " + employeeId);
        } else {
            employeeService.deleteById(employeeId);
            return "Deleted employee id - " + employeeId;
        }
    }
}
