package com.leemccormick.springcoredemo.common;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// @Component annotation marks the class as a Spring bean
// @Primary instead of @Qualifier to point to Primary Coach to run.
/*
- Only can use once other it will throw error -->
- 'more than one 'primary' bean found among candidates: [baseballCoach, cricketCoach, tennisCoach, trackCoach]'
- See more in TrackCoach
*/
// @Primary
@Component
public class CricketCoach implements Coach {

    // When init this constructor function will run
    public CricketCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }

    @Override
    public String getDailyWorkout() {
        return "Practice fast bowling for 15 minutes :-) :)";
    }
}
