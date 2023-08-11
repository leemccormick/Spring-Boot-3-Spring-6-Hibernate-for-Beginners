package com.leemccormick.springcoredemo.common;

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

// @Primary
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
