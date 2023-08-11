package com.leemccormick.cruddemo.dao;

import com.leemccormick.cruddemo.entity.Student;

import java.util.List;

// STEP 1) Create a new interface: StudentDAO
public interface StudentDAO {
    // Create Method
    void save(Student theStudent);

    // Read Method
    Student findById(Integer id);

    List<Student> findAll();

    List<Student> findByLastName(String theLastName);

    // Update Method
    void update(Student theStudent);

    // Delete Method
    void delete(Integer id);

    int deleteAll();
}
