package com.leemccomick.springbooy.cruddemo.dao;

import com.leemccomick.springbooy.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* HOW TO USE --> Spring DATA JPA (Get CRUD Methods for free)
1) Delete DAO
2) Create new class "EmployeeRepository" and extends JpaRepository
3) Use EmployeeRepository in EmployeeServiceImpl
*/

// <Employee, Integer> --> Employee == EntityType, Integer == Primary Key
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // That's it. No need to write any code. 🎉
}
