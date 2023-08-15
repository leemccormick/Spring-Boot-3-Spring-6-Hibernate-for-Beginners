package com.leemccormick.validationdemo.mvc.validation;

/* Custom Validation
1) Create annotation
2) Define attribute in annotation interface
3) Create CourseCodeConstraintValidator Class and implement interface
4) Add Custom Validation to Customer Class
5) display error message on HTML form
6) update confirmation page
*/

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CourseCodeConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseCode {
    // Define default course code
    public String value() default "LUV";

    // Define default error message
    public String message() default "must start with LUV";

    // Define default groups
    public Class<?>[] groups() default {};

    // Define default payload
    public Class<? extends Payload>[] payload() default {};
}
