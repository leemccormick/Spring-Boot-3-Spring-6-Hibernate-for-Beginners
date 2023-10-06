package com.leemccormick.posdemo.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leemccormick.posdemo.entity.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON response

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, jakarta.servlet.ServletException {
        log.error(String.format("ERROR : CustomAuthenticationEntryPoint | commence | authException --> %s", authException.getMessage()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ApiResponse errorResponse = new ApiResponse(true, response.getStatus(), authException.getMessage() + " : You are not authorized to access this info.");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}