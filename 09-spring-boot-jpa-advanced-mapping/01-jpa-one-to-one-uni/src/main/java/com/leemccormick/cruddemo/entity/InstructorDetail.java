package com.leemccormick.cruddemo.entity;

import jakarta.persistence.*;

/* One to One | Bi-Directional --> To find Instructor object from InstructorDetail object
1) Make update to InstructorDetail Class
1.1) Add new field to reference Instructor
1.2) Add getter/setter methods for Instructor
1.3) Add @OneToOne annotation
2) Create Main App
*/

// 1) Annotate the class as entity adn map to db table
// name = "instructor_detail" -> Have to be the same table name in the database
@Entity
@Table(name = "instructor_detail")
public class InstructorDetail {
    // 2) Define the fields
    // 3) Annotate the fields with db column names
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "youtubeChannel")
    private String youtubeChannel;

    @Column(name = "hobby")
    private String hobby;

    // 4) Create Constructors
    public InstructorDetail() {

    }

    public InstructorDetail(String youtubeChannel, String hobby) {
        this.youtubeChannel = youtubeChannel;
        this.hobby = hobby;
    }

    // 5) Generate getter/setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }


    // 6) Generate toString() method
    @Override
    public String toString() {
        return "InstructorDetail{" +
                "id=" + id +
                ", youtubeChannel='" + youtubeChannel + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
