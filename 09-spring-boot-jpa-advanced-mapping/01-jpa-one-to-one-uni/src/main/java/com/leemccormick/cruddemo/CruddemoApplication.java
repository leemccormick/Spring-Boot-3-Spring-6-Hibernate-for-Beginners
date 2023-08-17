package com.leemccormick.cruddemo;

import com.leemccormick.cruddemo.dao.AppDAO;
import com.leemccormick.cruddemo.entity.Instructor;
import com.leemccormick.cruddemo.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
            deleteInstructor(appDAO);
        };
    }

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
        // appDAO.save(tempInstructor);
        appDAO.save(tempInstructor1);
    }

    private void findInstructor(AppDAO appDAO) {
        int theId = 2;
        System.out.println("Finding the instructor with id: " + theId);

        Instructor tempInstructor = appDAO.findInstructorById(theId);
        System.out.println("Found the instructor : " + tempInstructor);
        System.out.println("Found the instructor and the associated instructorDetail Only : " + tempInstructor.getInstructorDetail());
    }

    private void deleteInstructor(AppDAO appDAO) {
        int theId = 2;
        System.out.println("Deleting the instructor with id: " + theId);

        appDAO.deleteInstructorById(theId);
        System.out.println("Finished Deleting the instructor with id: " + theId);
    }
}
