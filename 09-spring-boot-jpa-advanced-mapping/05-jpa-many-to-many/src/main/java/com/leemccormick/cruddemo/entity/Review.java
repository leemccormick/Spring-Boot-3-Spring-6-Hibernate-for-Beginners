package com.leemccormick.cruddemo.entity;

/* One-To-Many : Uni-Directional
1) Prep Work - Define database tables --> Create Review table in database with new script
2) Create Review class
3) Update Course class --> Configure one-to-many: Course --> Review by adding the reviews property and addReview() function
*/

import jakarta.persistence.*;

@Entity
@Table(name = "review")
public class Review {
    // Define the fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "comment")
    private String comment;

    // Constructors
    public Review() {

    }

    public Review(String comment) {
        this.comment = comment;
    }

    // Generate getter/setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Generate toString() method
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                '}';
    }
}
