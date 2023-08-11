package com.leemccormick.springcoredemo.common;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// @Primary instead of @Qualifier to point to Primary Coach to run, only can use once other it will throw error
@Component
 @Primary
public class TrackCoach implements Coach {
    @Override
    public String getDailyWorkout() {
        return "Run a hard 5k";
    }
}
