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
    // private Coach anotherCoach; --> Uncomment this for demo Singleton : Bean Scopes

    // Define a constructor for dependency injection
    // @Autowired annotation tells Spring to inject a dependency, if you have one constructor then @Autowired on constructor is optional.
    // @Primary instead of @Qualifier to point to Primary Coach to run, we do not need @Qualifier --> See example in TrackCoach
    /*
    @Autowired
    public DemoController(Coach theCoach) {
        myCoach = theCoach;
    }
    */

    // @Qualifier --> Specify the bean id: baseballCoach, same name as class, first character lower-case to point to baseballCoach
    // public DemoController(@Qualifier("baseballCoach") Coach theCoach) {
    // public DemoController(@Qualifier("tennisCoach") Coach theCoach) {
    // public DemoController(@Qualifier("trackCoach") Coach theCoach) {
    /*
    @Autowired
    public DemoController(@Qualifier("cricketCoach") Coach theCoach) {
        System.out.println("In constructor: " + getClass().getSimpleName());
        myCoach = theCoach;
    }
    */

    // Bean Scopes
    /*
    public DemoController(
            @Qualifier("cricketCoach") Coach theCoach,
            @Qualifier("cricketCoach") Coach theAnotherCoach
    ) {
        System.out.println("In constructor: " + getClass().getSimpleName());
        myCoach = theCoach;
        anotherCoach = theAnotherCoach;
    }
    */

    /* Bean Scopes
     - Check to see if this is the same bean, True or False depending on the bean scope.
     - Singleton : true
     - Prototype : false
     */
    /*
    @GetMapping("/check")
    public String check() {
        return "Comparing beans : myCoach == anotherCoach, " + (myCoach == anotherCoach);
    }
    */

    // Setter Injection
    // We can use any method name for setCoach()
    /*
    @Autowired
    public void doSomething(Coach theCoach) {
        myCoach = theCoach;
    }

    public void setCoach(Coach theCoach) {
        myCoach = theCoach;
    }
    */

    // Bean Lifecycle Methods
    @Autowired
    public DemoController(@Qualifier("cricketCoach") Coach theCoach) {
        System.out.println("In constructor: " + getClass().getSimpleName());
        myCoach = theCoach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return myCoach.getDailyWorkout();
    }
}
