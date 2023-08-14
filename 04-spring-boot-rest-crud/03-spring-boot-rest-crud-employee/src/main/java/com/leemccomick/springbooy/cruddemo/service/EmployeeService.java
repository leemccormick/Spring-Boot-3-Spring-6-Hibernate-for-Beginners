package com.leemccomick.springbooy.cruddemo.service;

import com.leemccomick.springbooy.cruddemo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    // NOTE : The methods are the same as EmployeeDAO
    List<Employee> findAll();

    Employee findById(int theId);

    Employee save(Employee theEmployee);

    void deleteById(int theId);
}
