package com.leemccormick.cruddemo.entity;

import jakarta.persistence.*;

/* One to One | Bi-Directional --> To find Instructor object from InstructorDetail object
1) Make update to InstructorDetail Class
1.1) Add new field to reference Instructor
1.2) Add getter/setter methods for Instructor
1.3) Add @OneToOne annotation
2) Create Main App
2.1) Update DAO Interface
2.2) Update DAO Impl
2.3) Call in Main App
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

    // One to One | Bi-Directional --> Add new field to reference Instructor and Add @OneToOne annotation
     /*
     - mappedBy = "instructorDetail" --> name of the filed in Instructor object
     - cascade = CascadeType.ALL --> Cascade all operations to the associated instructor
     - @OneToOne(mappedBy = "instructorDetail", cascade = CascadeType.ALL)
     - cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} --> Choose everything except remove
     */
    @OneToOne(mappedBy = "instructorDetail", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Instructor instructor;

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

    // One to One | Bi-Directional --> Add getter/setter methods for Instructor
    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
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
