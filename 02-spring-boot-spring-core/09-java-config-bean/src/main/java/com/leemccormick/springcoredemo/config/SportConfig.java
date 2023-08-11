package com.leemccormick.springcoredemo.config;

// Java Config Bean
/*
- 1) Create a new class : SportConfig --> add @Configuration to the class
- 2) Define @Bean method to configure the bean --> add @Bean and methods to return an object
*/

import com.leemccormick.springcoredemo.common.Coach;
import com.leemccormick.springcoredemo.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportConfig {

    // We can only use  @Bean or give a custom bean id
    // @Bean
    @Bean("aquatic")
    public Coach swimCoach() {
        return new SwimCoach();
    }
}
