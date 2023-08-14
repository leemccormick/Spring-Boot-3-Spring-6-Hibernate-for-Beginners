package com.leemccomick.springbooy.cruddemo.dao;

import com.leemccomick.springbooy.cruddemo.entity.Employee;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO {
    // 1) Define field for entityManager
    private EntityManager entityManager;

    // 2) Set up constructor injection
    @Autowired
    public EmployeeDAOJpaImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    // 3) Implement the EmployeeDAO Interface
    @Override
    public List<Employee> findAll() {
        // 1) Create a query
        TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee", Employee.class);

        // 2) Execute query and get result list
        List<Employee> employees = theQuery.getResultList();

        // 3) Return the results
        return employees;
    }

    @Override
    public Employee findById(int theId) {
        // 1) Get employee
        Employee theEmployee = entityManager.find(Employee.class, theId);

        // 2) Return employee
        return theEmployee;
    }

    @Override
    public Employee save(Employee theEmployee) {
        // 1) Save employee
        // merge --> for creating new data or updating existing data
        Employee dbEmployee = entityManager.merge(theEmployee);

        // 2) Return the dbEmployee
        return dbEmployee;
    }

    @Override
    public void deleteById(int theId) {
        // 1) Find employee with id
        Employee theEmployee = entityManager.find(Employee.class, theId);

        // 2) Delete the employee
        entityManager.remove(theEmployee);
    }
}
