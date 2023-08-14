package com.leemccomick.springbooy.cruddemo.service;

import com.leemccomick.springbooy.cruddemo.dao.EmployeeRepository;
import com.leemccomick.springbooy.cruddemo.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/* --> These code before using Spring Data JPA
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeDAO employeeDAO;

    public EmployeeServiceImpl(EmployeeDAO theEmployeeDAO) {
        employeeDAO = theEmployeeDAO;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(int theId) {
        return employeeDAO.findById(theId);
    }

    @Transactional // Use this Transactional on Service Layer to allow update database
    @Override
    public Employee save(Employee theEmployee) {
        return employeeDAO.save(theEmployee);
    }

    @Transactional // Use this Transactional on Service Layer to allow update database
    @Override
    public void deleteById(int theId) {
        employeeDAO.deleteById(theId);
    }
}
*/

// Using Spring Data JPA --> Replace this employeeRepository to the class
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository) {
        employeeRepository = theEmployeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        } else {
            // We did not find the employee
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theEmployee;
    }

    // @Transactional --> JPA provide this functionality out of the box
    @Override
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    // @Transactional --> JPA provide this functionality out of the box
    @Override
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }
}