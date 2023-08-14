package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {
    /* This code before create User and Authority Tables in Database
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
   */

    // Add support for JDBC, no more hard code users --> This function will auto look for users from database.
   /*
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
   */

    // To modify table name that match any table name in database
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // 1) Init Object
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // 2) Define query to retrieve a user by username, using regular SQL
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?"
        );

        // 3) Define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?"
        );

        // 4) Return a jdbcUserDetailsManager object
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1) Configure the role access
        http.authorizeHttpRequests(configure ->
                configure
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE") // READ
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE") // READ
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER") // CREATE AND UPDATE
                        .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER") // CREATE AND UPDATE
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN") // DELETE
        );

        // 2) Use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // 3) Disable Cross Site Request Forgery (CSRF)
        // In general, not required for stateless REST APIs that use POST, PUT, DELETE and / or PATCH
        http.csrf(csrf -> csrf.disable());

        // 4) Return value of SecurityFilterChain
        return http.build();
    }
}
