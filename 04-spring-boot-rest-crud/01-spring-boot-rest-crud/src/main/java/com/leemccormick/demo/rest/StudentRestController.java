package com.leemccormick.demo.rest;

import com.leemccormick.demo.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {
    private List<Student> theStudents;

    // Define @PostConstruct to load the student data ... only once!
    @PostConstruct
    public void loadData() {
        theStudents = new ArrayList<>();
        theStudents.add(new Student("Lee", "McCormick"));
        theStudents.add(new Student("Poornima", "Patel"));
        theStudents.add(new Student("John", "Doe"));
    }

    // 1) Define endpoint for "/students" - return a list of students
    @GetMapping("/students")
    public List<Student> getStudents() {
        /* --> Use loadData() to load once
        List<Student> theStudents = new ArrayList<>();
        theStudents.add(new Student("Lee", "McCormick"));
        theStudents.add(new Student("Poornima", "Patel"));
        theStudents.add(new Student("John", "Doe"));
         */
        return theStudents;
    }

    // 2) Define endpoint for "/students/{studentId}" - return a student at index
    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable int studentId) {
        // Check the studentId again list size
        if (studentId >= theStudents.size() || (studentId < 0)) {
            throw new StudentNotFoundException("Student id not found - " + studentId);
        }

        // Just index into the list, keep it simple for now...
        return theStudents.get(studentId);
    }

    // 3) Add an exception handler annotation
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handlerException(StudentNotFoundException exc) {
        // Create a StudentErrorResponse
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        // Return ResponseEntity
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 4) Add an exception handler annotation to catch any exception (catch all) using Exception exc
    @ExceptionHandler
    ResponseEntity<StudentErrorResponse> handlerException(Exception exc) {
        // Create a StudentErrorResponse
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        // Return ResponseEntity
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
