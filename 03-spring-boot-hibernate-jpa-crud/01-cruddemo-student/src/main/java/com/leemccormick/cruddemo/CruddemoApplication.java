package com.leemccormick.cruddemo;

import com.leemccormick.cruddemo.dao.StudentDAO;
import com.leemccormick.cruddemo.entity.Student;
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

    // Connect with the Bean
	/*
	@Bean
	public CommandLineRunner commandLineRunner(String[] args) {
		// This is a Java Lambda expression --> It's simply like a shortcut notation for providing an implementation of the CommandLineRunner interface.
		return runner -> {
			System.out.println("Hello World");
		};
	}
	*/

    // STEP 3) Update main app to save Student in Database
	/*
	- See step 1 at StudentDAO
	- See step 2 at StudentDAOImpl
	*/
    @Bean
    public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {
        return runner -> {
            createMultipleStudents(studentDAO);
            // createStudent(studentDAO);
            // readStudent(studentDAO);
            // queryForStudents(studentDAO);
            // queryForStudentsByLastName(studentDAO);
            // updateStudent(studentDAO);
            // deleteStudent(studentDAO);
            // deleteAll(studentDAO);
        };
    }

    // CRUD
    // CREATE METHODS
    private void createMultipleStudents(StudentDAO studentDAO) {
        // 1) Create the student objects
        System.out.println("Creating 3 new students object...");
        Student tempStudent1 = new Student("John", "Doe", "john@luv2code.com");
        Student tempStudent2 = new Student("Lee", "McCormick", "lee@luv2code.com");
        Student tempStudent3 = new Student("Bonita", "Applebum", "bonita@luv2code.com");

        // 2) Save the student objects
        System.out.println("Saving 3 student objects...");
        studentDAO.save(tempStudent1);
        studentDAO.save(tempStudent2);
        studentDAO.save(tempStudent3);

        // 3) Display id of the saved student objects
        System.out.println("Saved tempStudent1 student, Generated id: " + tempStudent1.getId());
        System.out.println("Saved tempStudent2 student, Generated id: " + tempStudent2.getId());
        System.out.println("Saved tempStudent3 student, Generated id: " + tempStudent3.getId());
    }

    private void createStudent(StudentDAO studentDAO) {
        // 1) Create the student object
        System.out.println("Creating new student object...");
        Student tempStudent = new Student("Paul", "Doe", "paul@luv2code.com");

        // 2) Save the student object
        System.out.println("Saving the student object...");
        studentDAO.save(tempStudent);

        // 3) Display id of the saved student object
        System.out.println("Saved student, Generated id: " + tempStudent.getId());
    }

    // READ METHODS
    private void readStudent(StudentDAO studentDAO) {
        // 1) Create the student object
        System.out.println("READ - Creating new student object...");
        Student tempStudent = new Student("Daffy", "Duck", "daffy@luv2code.com");

        // 2) Save the student object
        System.out.println("READ - Saving the student object...");
        studentDAO.save(tempStudent);

        // 3) Display id of the saved student object
        int theId = tempStudent.getId();
        System.out.println("READ - Saved student, Generated id: " + theId);

        // 4) Retrieve student based on the id: primary key
        System.out.println("READ - Retrieving Saved student with id: " + theId);
        Student myStudent = studentDAO.findById(theId);

        // 5) Display student
        System.out.println("READ - Found Saved student : " + myStudent.toString());
    }

    private void queryForStudents(StudentDAO studentDAO) {
        // 1) Get a list of students
        List<Student> theStudents = studentDAO.findAll();

        // 2) Display list of students
        for (Student tempStudent : theStudents) {
            System.out.println("The student from list : " + tempStudent.toString());
        }
    }

    private void queryForStudentsByLastName(StudentDAO studentDAO) {
        // 1) Get a list of students by last name
        List<Student> theStudents = studentDAO.findByLastName("Duck");

        // 2) Display list of students who have last name as "Duck"
        for (Student tempStudent : theStudents) {
            System.out.println("The student from findByLastName : " + tempStudent.toString());
        }
    }

    // UPDATE METHODS
    private void updateStudent(StudentDAO studentDAO) {
        // 1) Retrieve student based on the id: primary key
        int studentId = 1;
        System.out.println("UPDATE - Getting student with id : " + studentId);
        Student myStudent = studentDAO.findById(studentId);

        // 2) Change first name to "Scooby"
        System.out.println("UPDATE - Before changing student first name :  " + myStudent.getFirstName());
        myStudent.setFirstName("Scooby");

        // 3) Update the student
        System.out.println("UPDATE - Changing student first name :  " + myStudent.toString());
        studentDAO.update(myStudent);

        // 4) Display the updated student
        System.out.println("UPDATE - After changing student first name :  " + myStudent.getFirstName());
        System.out.println("UPDATE - After updating the student :  " + myStudent.toString());
    }

    // DELETE METHODS
    private void deleteStudent(StudentDAO studentDAO) {
        // 1) Retrieve student based on the id: primary key
        int studentId = 3;
        System.out.println("DELETE - student with id : " + studentId);

        // 2) Delete the student by id
        studentDAO.delete(studentId);
    }

    private void deleteAll(StudentDAO studentDAO) {
        System.out.println("DELETE - deleting all students");
        int numRowsDeleted = studentDAO.deleteAll();
        System.out.println("DELETE - deleted row count : " + numRowsDeleted);
    }
}
