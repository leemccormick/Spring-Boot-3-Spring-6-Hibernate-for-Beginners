package com.leemccormick.springcoredemo.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// @Component annotation marks the class as a Spring bean

// @Primary instead of @Qualifier to point to Primary Coach to run.
/*
- Only can use once other it will throw error -->
- 'more than one 'primary' bean found among candidates: [baseballCoach, cricketCoach, tennisCoach, trackCoach]'
- See more in TrackCoach
*/

// @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
/*
- By default SCOPE_SINGLETON, if we don't inject this @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
- To get rid of singleton, then we need to inject --> @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
- See more in DemoController, when we refer to myCoach and anotherCoach at Check()
*/

// @PostConstruct --> For lifecycle methods
// @PreDestroy --> For lifecycle methods
/*
- @PostConstruct --> init method, when the class get initialized this function get called --> do some start up tasks here...
- @PreDestroy --> destroy method, when the application get stopped this function get called --> do some clean up tasks here...
*/

// Special Note about Prototype Scope - Destroy Lifecycle Method and Lazy Init
/*
- Prototype Beans and Destroy Lifecycle --> There is a subtle point you need to be aware of with "prototype" scoped beans.
- For "prototype" scoped beans, Spring does not call the destroy method. Gasp!
- In contrast to the other scopes, Spring does not manage the complete lifecycle of a prototype bean: the container instantiates, configures, and otherwise assembles a prototype object, and hands it to the client, with no further record of that prototype instance.
- Thus, although initialization lifecycle callback methods are called on all objects regardless of scope, in the case of prototypes, configured destruction lifecycle callbacks are not called. The client code must clean up prototype-scoped objects and release expensive resources that the prototype bean(s) are holding.
- Prototype Beans and Lazy Initialization --> Prototype beans are lazy by default. There is no need to use the @Lazy annotation for prototype scopes beans.
*/

// @Primary
// @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class CricketCoach implements Coach {

    // When init this constructor function will run
    public CricketCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }

    // Define our init method
    @PostConstruct
    public void doMyStartupStuff() {
        System.out.println("In doMyStartupStuff: " + getClass().getSimpleName());
    }

    // Define our destroy method
    @PreDestroy
    public void doMyCleanupStuff() {
        System.out.println("In doMyCleanupStuff: " + getClass().getSimpleName());
    }

    // This is the implement from interface Coach --> Similar to protocol in swift
    @Override
    public String getDailyWorkout() {
        return "Practice fast bowling for 15 minutes :-) :)";
    }
}
