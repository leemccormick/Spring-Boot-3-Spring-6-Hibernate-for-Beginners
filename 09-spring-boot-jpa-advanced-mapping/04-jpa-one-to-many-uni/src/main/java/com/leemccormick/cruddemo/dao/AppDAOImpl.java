package com.leemccormick.cruddemo.dao;

import com.leemccormick.cruddemo.entity.Course;
import com.leemccormick.cruddemo.entity.Instructor;
import com.leemccormick.cruddemo.entity.InstructorDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        // 2) Get the courses
        List<Course> courses = tempInstructor.getCourses();

        // 2.1) Break association of all course for the instructor to remove the instructor from the course
        for (Course tempCourse : courses) {
            tempCourse.setInstructor(null);
        }

        // 3) Delete the instructor
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
        InstructorDetail tempInstructorDetail = entityManager.find(InstructorDetail.class, theId);

        // 2) Remove the associate object reference and break bi-directional link
        tempInstructorDetail.getInstructor().setInstructorDetail(null);

        // 3) Delete the instructor detail
        entityManager.remove(tempInstructorDetail);
    }

    @Override
    public List<Course> findCoursesByInstructorId(int theId) {
        // 1) Create query
        TypedQuery<Course> query = entityManager.createQuery(
                "from Course where instructor.id = :data",
                Course.class
        );

        // 2) Set Parameter
        query.setParameter("data", theId);

        // 3) Execute the query
        List<Course> courses = query.getResultList();
        return courses;
    }

    /* JOIN FETCH QUERY
    - JOIN FETCH is similar to EAGER LOADING
    - Even fetchType is LAZY, the code will still retrieve Instructor AND Courses using JOIN FETCH
    */
    @Override
    public Instructor findInstructorByIdJoinFetch(int theId) {
        // 1) Create query with JOIN FETCH
        TypedQuery<Instructor> query = entityManager.createQuery(
                "select i from Instructor i "
                        + "JOIN FETCH i.courses "
                        + "JOIN FETCH i.instructorDetail "
                        + "where i.id = :data",
                Instructor.class
        );

        // 2) Set Parameter
        query.setParameter("data", theId);

        // 3) Execute the query
        Instructor instructor = query.getSingleResult();
        return instructor;
    }

    @Override
    @Transactional
    public void update(Instructor tempInstructor) {
        entityManager.merge(tempInstructor);
    }

    @Override
    @Transactional
    public void update(Course tempCourse) {
        entityManager.merge(tempCourse);
    }

    @Override
    public Course findCourseById(int theId) {
        return entityManager.find(Course.class, theId);
    }

    @Override
    @Transactional
    public void deleteCourseById(int theId) {
        // 1) Retrieve the course
        Course tempCourse = entityManager.find(Course.class, theId);

        // 2) Delete the course
        entityManager.remove(tempCourse);
    }

    @Override
    @Transactional
    public void save(Course theCourse) {
        // This will save the course and reviews because the cascade = CascadeType.ALL in Course Object
        entityManager.persist(theCourse);
    }

    @Override
    public Course findCourseAndReviewsByCourseId(int theId) {
        // 1) Create query with JOIN FETCH
        TypedQuery<Course> query = entityManager.createQuery(
                "select c from Course c "
                        + "JOIN FETCH c.reviews "
                        + "where c.id = :data",
                Course.class
        );

        // 2) Set Parameter
        query.setParameter("data", theId);

        // 3) Execute the query
        return query.getSingleResult();
    }
}
