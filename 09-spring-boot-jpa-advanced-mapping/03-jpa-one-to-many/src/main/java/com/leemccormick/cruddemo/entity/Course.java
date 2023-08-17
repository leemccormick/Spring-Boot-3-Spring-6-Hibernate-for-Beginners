package com.leemccormick.cruddemo.entity;

import jakarta.persistence.*;

/* One-To-Many
1) Prep Work - Define database tables with course
2) Create Course class
3) Update Instructor class : See more Step 8 to add property and add function
4) Create Main App
4.1) Point to new schema on application.properties
*/

// 1) Annotate the class as entity adn map to db table
@Entity
@Table(name = "course")
public class Course {
    // 2) Define the fields
    // 3) Annotate the fields with db column names
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // 4) Set up relationship --> set up mapping to Instructor Entity
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @Column(name = "title")
    private String title;

    // 5) Create Constructors
    public Course() {

    }

    public Course(String title) {
        this.title = title;
    }

    // 6) Generate getter/setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // 7) Generate toString() method
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", instructor=" + instructor +
                ", title='" + title + '\'' +
                '}';
    }
}
