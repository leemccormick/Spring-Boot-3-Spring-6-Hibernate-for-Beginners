package com.leemccormick.cruddemo;

import com.leemccormick.cruddemo.dao.AppDAO;
import com.leemccormick.cruddemo.entity.Course;
import com.leemccormick.cruddemo.entity.Instructor;
import com.leemccormick.cruddemo.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CruddemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CruddemoApplication.class, args);
    }

	/* Before implement the DAO, use this code to test if the application running...
	@Bean
	public CommandLineRunner commandLineRunner(String[] args) {
		return  runner -> {
			System.out.println("Hello World");
		};
	}
	*/

    // Implement AppDAO
    @Bean
    public CommandLineRunner commandLineRunner(AppDAO appDAO) {
        return runner -> {
            System.out.println("Implementing AppDAO....");
            // createInstructor(appDAO);
            // findInstructor(appDAO);
            // deleteInstructor(appDAO);
            // findInstructorDetail(appDAO);
            // deleteInstructorDetail(appDAO);
            // createInstructorWithCourse(appDAO);
            // findInstructorWithCourses(appDAO);
            // findCoursesForInstructor(appDAO);
            // findInstructorWithCoursesJoinFetch(appDAO);
            // updateInstructor(appDAO);
            // updateCourse(appDAO);
            deleteCourse(appDAO);
        };
    }

    // CREATE Methods
    private void createInstructor(AppDAO appDAO) {
        // 1) Create Instructor
        Instructor tempInstructor = new Instructor("Chad", "Darby", "darby@email.com");
        Instructor tempInstructor1 = new Instructor("Lee", "Mc", "lee@email.com");

        // 2) Create Instructor Details
        InstructorDetail tempInstructorDetail = new InstructorDetail("http://www.luv2code.com/youtube", "Luv 2 code");
        InstructorDetail tempInstructorDetail1 = new InstructorDetail("http://www.leemc.com/youtube", "Run");

        // 3) Associate the objects
        tempInstructor.setInstructorDetail(tempInstructorDetail);
        tempInstructor1.setInstructorDetail(tempInstructorDetail1);

        // 4) Save the instructor --> This will also save the details object because of CascadeType.ALL
        System.out.println("Saving the instructor: " + tempInstructor);
        appDAO.save(tempInstructor);
        appDAO.save(tempInstructor1);
    }

    private void createInstructorWithCourse(AppDAO appDAO) {
        // 1) Create Instructor
        Instructor tempInstructor = new Instructor("Chad", "Darby", "darby@email.com");
        Instructor tempInstructor1 = new Instructor("Lee", "Mc", "lee@email.com");
        Instructor tempInstructor2 = new Instructor("Susan", "Public", "susan@email.com");

        // 2) Create Instructor Details
        InstructorDetail tempInstructorDetail = new InstructorDetail("http://www.luv2code.com/youtube", "Luv 2 code");
        InstructorDetail tempInstructorDetail1 = new InstructorDetail("http://www.leemc.com/youtube", "Run");
        InstructorDetail tempInstructorDetail2 = new InstructorDetail("http://www.susan.com/youtube", "Hiking");


        // 3) Associate the objects
        tempInstructor.setInstructorDetail(tempInstructorDetail);
        tempInstructor1.setInstructorDetail(tempInstructorDetail1);
        tempInstructor2.setInstructorDetail(tempInstructorDetail2);

        // 4) Create some courses and add to instructor
        Course tempCourse1 = new Course("Air Guitar - The Ultimate Guide");
        Course tempCourse2 = new Course("The Pinball Masterclass");
        tempInstructor.add(tempCourse1);
        tempInstructor.add(tempCourse2);

        // 5) Save the instructor -->
        // - This will also save the details object because of CascadeType.ALL
        // - This will also save the courses object because of CascadeType.PERSIST
        System.out.println("Saving the instructor: " + tempInstructor);
        appDAO.save(tempInstructor);
        appDAO.save(tempInstructor1);
        appDAO.save(tempInstructor2);
    }

    // READ Methods
    private void findInstructor(AppDAO appDAO) {
        int theId = 1;
        System.out.println("Finding the instructor with id: " + theId);

        Instructor tempInstructor = appDAO.findInstructorById(theId);
        System.out.println("Found the instructor : " + tempInstructor);
        System.out.println("Found the instructor and the associated instructorDetail Only : " + tempInstructor.getInstructorDetail());
    }

    private void findInstructorDetail(AppDAO appDAO) {
        // 1) Get the instructor detail object
        int theId = 1;
        System.out.println("Finding the instructor detail with id: " + theId);

        // 2) Print the instructor detail
        InstructorDetail tempInstructorDetail = appDAO.findInstructorDetailById(theId);
        System.out.println("Found the instructorDetail : " + tempInstructorDetail);

        // 3) Print the associated instructor
        System.out.println("Found the associated instructor Only : " + tempInstructorDetail.getInstructor());
    }

    private void findInstructorWithCourses(AppDAO appDAO) {
        int theId = 1;
        System.out.println("findInstructorWithCourses() | Finding the instructor with id: " + theId);

        Instructor tempInstructor = appDAO.findInstructorById(theId);

        System.out.println("findInstructorWithCourses() | Found the instructor : " + tempInstructor);
        System.out.println("findInstructorWithCourses() | Found the instructor and the associated courses Only : " + tempInstructor.getCourses());
        System.out.println("findInstructorWithCourses() | DONE");
    }

    private void findCoursesForInstructor(AppDAO appDAO) {
        int theId = 1;
        System.out.println("findCoursesForInstructor() | Finding the instructor with id: " + theId);

        Instructor tempInstructor = appDAO.findInstructorById(theId);
        System.out.println("findCoursesForInstructor() | Found the instructor : " + tempInstructor);

        System.out.println("findCoursesForInstructor() | Finding the courses with instructor id: " + theId);

        List<Course> courses = appDAO.findCoursesByInstructorId(theId);
        System.out.println("findCoursesForInstructor() | Found the courses : " + courses);
        System.out.println("findCoursesForInstructor() | DONE");
    }


    private void findInstructorWithCoursesJoinFetch(AppDAO appDAO) {
        int theId = 1;
        System.out.println("findInstructorWithCoursesJoinFetch() | Finding the instructor with id: " + theId);

        Instructor tempInstructor = appDAO.findInstructorByIdJoinFetch(theId);
        System.out.println("findInstructorWithCoursesJoinFetch() | Found the instructor : " + tempInstructor);
        System.out.println("findInstructorWithCoursesJoinFetch() | Found the courses : " + tempInstructor.getCourses());
        System.out.println("findInstructorWithCoursesJoinFetch() | DONE");
    }

    // UPDATE Methods
    private void updateInstructor(AppDAO appDAO) {
        // 1) Find the instructor
        int theId = 2;
        System.out.println("updateInstructor() | Finding the instructor with id: " + theId);

        Instructor tempInstructor = appDAO.findInstructorById(theId);
        System.out.println("updateInstructor() | Found the instructor : " + tempInstructor);

        // 2) Update the instructor
        System.out.println("updateInstructor() | Updating the instructor : " + tempInstructor);
        tempInstructor.setLastName("TESTER");
        tempInstructor.setEmail("tester@email.com");

        appDAO.update(tempInstructor);
        System.out.println("updateInstructor() | Updated the instructor : " + tempInstructor);
    }

    private void updateCourse(AppDAO appDAO) {
        // 1) Find the course
        int theId = 10;
        System.out.println("updateCourse() | Finding the course with id: " + theId);

        Course tempCourse = appDAO.findCourseById(theId);
        System.out.println("updateCourse() | Found the course : " + tempCourse);

        // 2) Update the course
        System.out.println("updateCourse() | Updating the course : " + tempCourse);
        tempCourse.setTitle("Enjoy the Spring Bott");

        appDAO.update(tempCourse);
        System.out.println("updateCourse() | Updated the course : " + tempCourse);
    }

    // DELETE Methods
    private void deleteInstructor(AppDAO appDAO) {
        int theId = 1;
        System.out.println("Deleting the instructor with id: " + theId);

        appDAO.deleteInstructorById(theId);
        System.out.println("Finished Deleting the instructor with id: " + theId);
    }

    private void deleteInstructorDetail(AppDAO appDAO) {
        int theId = 7;
        System.out.println("Deleting the instructor detail with id: " + theId);

        appDAO.deleteInstructorDetailById(theId);
        System.out.println("Finished Deleting the instructor detail with id: " + theId);
    }

    private void deleteCourse(AppDAO appDAO) {
        int theId = 10;
        System.out.println("deleteCourse() | Deleting the course with id: " + theId);

        appDAO.deleteCourseById(theId);
        System.out.println("deleteCourse() | Finished Deleting the course with id: " + theId);
    }
}
