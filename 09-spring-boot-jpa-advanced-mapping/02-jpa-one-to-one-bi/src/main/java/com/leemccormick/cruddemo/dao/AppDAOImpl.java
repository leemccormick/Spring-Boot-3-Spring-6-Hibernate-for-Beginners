package com.leemccormick.cruddemo.dao;

import com.leemccormick.cruddemo.entity.Instructor;
import com.leemccormick.cruddemo.entity.InstructorDetail;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Repository --> Very important to annotation this when implement DAO
@Repository
public class AppDAOImpl implements AppDAO {
    // 1) Define fields for entity manager
    private EntityManager entityManager;

    // 2) Inject entity manager using constructor injection
    @Autowired
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // 3) Override AppDAO implementation
    @Override
    @Transactional
    public void save(Instructor theInstructor) {
        entityManager.persist(theInstructor);
    }

    @Override
    public Instructor findInstructorById(int theId) {
        return entityManager.find(Instructor.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorById(int theId) {
        // 1) Retrieve the instructor
        Instructor tempInstructor = findInstructorById(theId);

        // 2) Delete the instructor
        entityManager.remove(tempInstructor);
    }

    @Override
    public InstructorDetail findInstructorDetailById(int theId) {
        return entityManager.find(InstructorDetail.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorDetailById(int theId) {
        // 1) Retrieve the instructor detail
        InstructorDetail tempInstructorDetail =  entityManager.find(InstructorDetail.class, theId);

        // 2) Remove the associate object reference and break bi-directional link
        tempInstructorDetail.getInstructor().setInstructorDetail(null);

        // 3) Delete the instructor detail
        entityManager.remove(tempInstructorDetail);
    }
}
