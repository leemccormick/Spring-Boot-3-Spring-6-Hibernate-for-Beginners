package com.leemccormick.posdemo.security;

import com.leemccormick.posdemo.aspect.CustomAccessDeniedHandler;
import com.leemccormick.posdemo.aspect.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // 1) Init Object
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // 2) Define query to retrieve a user by username, using regular SQL
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from users where username=?"
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
        // 1) Configure HTTP Authorization
        http.authorizeHttpRequests(configure ->
                        configure
                                .requestMatchers("/").hasRole("CUSTOMER")
                                .requestMatchers("/sellers/**").hasRole("SALE")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/mystoredemo/products/**").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/api/mystoredemo/products/**").hasAnyRole("SALE", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/mystoredemo/products/**").hasAnyRole("SALE", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/mystoredemo/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/mystoredemo/info/**").hasAnyRole("SALE", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/mystoredemo/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/mystoredemo/users/userDetails").hasAnyRole("CUSTOMER", "SALE", "ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout ->
                        logout.permitAll()
                )
                .exceptionHandling(configure ->
                                configure
                                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Set your custom entry point
                                        .accessDeniedHandler(customAccessDeniedHandler) // When user is not authorized then we go to this class instead of /access-denied.html
                        // .accessDeniedPage("/access-denied") --> This is original code to go to access-denied.html
                );

        // 2) Use HTTP Basic authentication --> Need this line for Basic Auth in PostMan
        http.httpBasic(Customizer.withDefaults());

        // 3) Disable Cross Site Request Forgery (CSRF)
        // In general, not required for stateless REST APIs that use POST, PUT, DELETE and / or PATCH
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}

