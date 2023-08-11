package com.leemccormick.springcoredemo.common;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// @Primary instead of @Qualifier to point to Primary Coach to run, only can use once other it will throw error
// @Lazy, it will not initialize until it needed.
/*
- We can set up Lazy-Initialization in Global Level, see more example in application.properties
*/
@Component
// @Primary
// @Lazy
public class TrackCoach implements Coach {

    // When init this constructor function will run
    public TrackCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }

    @Override
    public String getDailyWorkout() {
        return "Run a hard 5k";
    }
}
