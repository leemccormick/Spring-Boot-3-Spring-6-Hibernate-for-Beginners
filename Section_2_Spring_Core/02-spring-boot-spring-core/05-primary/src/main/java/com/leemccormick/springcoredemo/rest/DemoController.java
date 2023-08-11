package com.leemccormick.springcoredemo.rest;

import com.leemccormick.springcoredemo.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    // Define a private field for the dependency
    private Coach myCoach;

    // Define a constructor for dependency injection
    // @Autowired annotation tells Spring to inject a dependency, if you have one constructor then @Autowired on constructor is optional.
    // @Primary instead of @Qualifier to point to Primary Coach to run, we do not need @Qualifier --> See example in TrackCoach
    @Autowired
    public DemoController(Coach theCoach) {
        myCoach = theCoach;
    }

    /*
    // @Qualifier --> Specify the bean id: baseballCoach, same name as class, first character lower-case to point to baseballCoach
    @Autowired
    // public DemoController(@Qualifier("baseballCoach") Coach theCoach) {
    // public DemoController(@Qualifier("tennisCoach") Coach theCoach) {
    // public DemoController(@Qualifier("trackCoach") Coach theCoach) {
    public DemoController(@Qualifier("cricketCoach") Coach theCoach) {
        myCoach = theCoach;
    }
    */

    // Setter Injection
    @Autowired
    // We can use any method name for setCoach()
     /*
    public void doSomething(Coach theCoach) {
        myCoach = theCoach;
    }

    public void setCoach(Coach theCoach) {
        myCoach = theCoach;
    }
    */

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return myCoach.getDailyWorkout();
    }
}
