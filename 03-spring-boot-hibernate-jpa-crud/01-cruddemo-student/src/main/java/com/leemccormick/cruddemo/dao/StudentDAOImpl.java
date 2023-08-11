package com.leemccormick.cruddemo.dao;

import com.leemccormick.cruddemo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// STEP 2) Create the implementation for DAO: StudentDAOImpl with StudentDAO Interface
/*
- @Repository --> Specialized annotation for repositories, supports component scanning, translates JDBC exceptions
*/

@Repository
public class StudentDAOImpl implements StudentDAO {
    // 1) Define filed for entity manager
    private EntityManager entityManager;

    // 2) Inject entity manager using constructor injection
    @Autowired
    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // 3) Implement Create/Save method --> by @Transactional and use this code 'entityManager.persist(theStudent)'
    @Override
    @Transactional
    public void save(Student theStudent) {
        entityManager.persist(theStudent);
    }

    // 4) Implement Read method to find by id --> by use this code 'entityManager.find(Student.class, id)'
    @Override
    public Student findById(Integer id) {
        return entityManager.find(Student.class, id);
    }

    // 5) Implement Read method to find all student
    @Override
    public List<Student> findAll() {
        // 1) Create query
        /*
        - "From Student" --> Name of JPA Entity, the class name : This is not the name of the database table.
        - All JPQL syntax is based on entity name and entity fields.
        - Default: sort ascending --> if we don't worry about sorting, we can use this line of code : "From Student"
        */
        // - No Sorting Logic
        // TypedQuery<Student> theQuery = entityManager.createQuery("From Student", Student.class);
        // - With Sorting by lastName descending --> "From Student order by lastName desc"
        // TypedQuery<Student> theQuery = entityManager.createQuery("From Student order by lastName desc", Student.class);
        // - With Sorting by lastName ascending --> "From Student order by lastName" OR "From Student order by lastName asc"
        TypedQuery<Student> theQuery = entityManager.createQuery("From Student order by lastName asc", Student.class);

        // 2) Return query result
        return theQuery.getResultList();
    }

    // 6) Implement Read method to find by last name
    @Override
    public List<Student> findByLastName(String theLastName) {
        // 1) Create query find by last name
        TypedQuery<Student> theQuery = entityManager.createQuery(
                "From Student WHERE lastName=:theData", Student.class
        );

        // 2) Set query parameters --> Fill in theData for parameters
        theQuery.setParameter("theData", theLastName);

        // 3) Return query result
        return theQuery.getResultList();
    }

    // 7) Implement Update method to update a student object using @Transactional
    @Override
    @Transactional
    public void update(Student theStudent) {
        entityManager.merge(theStudent);
    }

    // 8) Implement Delete method to delete the student with id
    @Override
    @Transactional
    public void delete(Integer id) {
        // 1) Retrieve the student by id
        Student theStudent = entityManager.find(Student.class, id);
        // 2) Delete the student
        entityManager.remove(theStudent);
    }

    // 8) Implement Delete method to delete all students  from database
    /*
    - @Transactional --> Always add this annotation when modify or delete something from database.
    - @Transactional --> For Create, Update, Delete Methods
    - @Transactional --> Do not need for Read Methods
    */
    @Override
    @Transactional
    public int deleteAll() {
        int numRowsDeleted = entityManager.createQuery("DELETE FROM Student").executeUpdate();
        return numRowsDeleted;
    }
}
