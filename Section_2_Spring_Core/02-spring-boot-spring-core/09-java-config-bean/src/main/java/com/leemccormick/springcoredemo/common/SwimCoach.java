package com.leemccormick.springcoredemo.common;

// This SwimCoach --> to demo how to use Java Config Bean
/*
- @Component --> We are not using this on purpose.
- See more in the SportConfig
*/
public class SwimCoach implements Coach {

    public SwimCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }
    @Override
    public String getDailyWorkout() {
        return "Swim 1000 meters as a warm up";
    }
}
