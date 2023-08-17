package com.leemccormick.cruddemo.dao;

import com.leemccormick.cruddemo.entity.Instructor;
import com.leemccormick.cruddemo.entity.InstructorDetail;

public interface AppDAO {
    void save(Instructor theInstructor);

    Instructor findInstructorById(int theId);

    void deleteInstructorById(int theId);

    InstructorDetail findInstructorDetailById(int theId);
    void deleteInstructorDetailById(int theId);
}
