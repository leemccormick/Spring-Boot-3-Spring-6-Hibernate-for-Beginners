package com.luv2code.springboot.cruddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* Add security dependency to the project :
- It will add security to all endpoints
- Default user name : user
- Password: 6edcdb45-8106-47e6-abe9-ab747eece8df --> Always change, see this password in terminal
- This is the dependency on pom.xml -->
       <!-- Add security dependency -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
*/

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

}
