package com.leemccormick.validationdemo.mvc;

/* How to create validation Required Fields
STEP 1 : Create Customer Class and  add validation rule
STEP 2 : Add Controller code to show HTML form
STEP 3 : Develop HTML form and add validation support
STEP 4 : Perform validation in the Controller class
STEP 5 : Create confirmation page
*/

import com.leemccormick.validationdemo.mvc.validation.CourseCode;
import jakarta.validation.constraints.*;

public class Customer {
    private String firstName;

    // To add validation rule with @NotNull and @Size
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required ")
    private String lastName = "";

    // To add validation rules to Customer class
    /*
    - 1) add validation rules to Customer class
    - 2) display error message on HTML form
    - 3) preform processing in Controller --> No changes need it because we have processForm()
    - 4) update confirmation page
    */

    @NotNull(message = "is required")
    @Min(value = 0, message = "must be greater than or equal to zero")
    @Max(value = 10, message = "must be less than or equal to 10")
    private Integer freePasses;


    // To apply regular expression
    /*
    - 1) add validation rules to Customer class
    - 2) display error message on HTML form
    - 3) preform processing in Controller --> No changes need it because we have processForm()
    - 4) update confirmation page
    */
    @Pattern(regexp="^[a-zA-Z0-9]{5}", message = "only 5 chars/digits")
    private String postalCode;

    // To custom validation
    @CourseCode(value = "TOP", message = "must start with TOP")
    private String courseCode;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getFreePasses() {
        return freePasses;
    }

    public void setFreePasses(Integer freePasses) {
        this.freePasses = freePasses;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
