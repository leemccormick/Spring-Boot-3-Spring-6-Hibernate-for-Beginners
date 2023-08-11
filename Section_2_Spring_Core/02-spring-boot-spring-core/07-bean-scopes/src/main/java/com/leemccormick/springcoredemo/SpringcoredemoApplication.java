package com.leemccormick.springcoredemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Explicitly list base packages to scan --> By default @SpringBootApplication --> Only scan packages inside this folder com.leemccormick
/*
@SpringBootApplication(
		scanBasePackages = {
				"com.leemccormick.springcoredemo",
				"com.leemccormick.util",
		}
)
*/

@SpringBootApplication
public class SpringcoredemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcoredemoApplication.class, args);
    }

}
