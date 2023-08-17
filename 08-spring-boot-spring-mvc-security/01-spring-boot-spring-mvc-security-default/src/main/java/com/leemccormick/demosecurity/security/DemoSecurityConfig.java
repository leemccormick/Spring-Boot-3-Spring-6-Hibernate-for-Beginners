package com.leemccormick.demosecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/* To mock security data  -->
1) Create Configuration Class
2) Add dummy data for user and role by using InMemoryUserDetailsManager userDetailsManager()  and UserDetails...
*/

/* To create custom login form  -->
1) Modify String Security Configuration to reference custom login form --> @Bean public SecurityFilterChain() ...
2) Develop a Controller to show the custom login form --> @GetMapping("/showMyLoginPage") on LoginController
3) Create custom login form with HTML or CSS
4) Add Error Handle when failed to login --> Modify plain-login form --> if error then show message
5) We can use new style from bootstrap by copy the fancy-login.html for beautiful style
*/

/* To Logout -->
1) Add Logout support to Spring Security Configuration --> .logout(logout -> logout.permitAll());
2) Add logout button, we get it for free for POST logout action but need to add action to html file
3) Update login form to display "logged out message"
*/

/* How to Restrict URLs based on Roles
1) Create Supporting Controller code and View Pages -->
- 1.1) on home.html --> add a link to point to each role path
- 1.2) on DemoController --> add logic to display the link base on roles
- 1.3) Create home landing detail page html for each role --> ex. leaders.html
2) Restricting Access to Roles using requestMatchers()... --> in SecurityFilterChain()
*/

/* How to Create Custom Access Denied Page
1) Configure custom page access denied in SecurityFilterChain() --> .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
2) Create Supporting Controller code and View Page
*/

/* How to JDBC Authentication
1) Develop SQL Script to setup database tables --> username and authority tables and insert test data
2) Add Database Support to Maven POM file
3) Create JDBC Properties File in application.properties
4) Update Spring Security to user JDBC
*/
@Configuration
public class DemoSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails john = User.builder()
                .username("john")
                .password("{noop}test123")
                .roles("EMPLOYEE")
                .build();

        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}test123")
                .roles("EMPLOYEE", "MANAGER")
                .build();

        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}test123")
                .roles("EMPLOYEE", "MANAGER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasRole("EMPLOYEE")
                                .requestMatchers("/leaders/**").hasRole("MANAGER")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout ->
                        logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied") // access-denied --> we can named it to anything else...
                );

        return http.build();
    }
}
