package com.leemccormick.posdemo.aspect;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.leemccormick.posdemo.entity.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON response

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException, IOException {
        if (isApiRequest(request)) {
            // If it's an API request, return a JSON response
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            ApiResponse errorResponse = new ApiResponse(true, response.getStatus(), accessDeniedException.getMessage() + " : You are not authorized to access this info.");
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } else {
            // If it's not an API request, redirect to an HTML page (e.g., access-denied.html)
            response.sendRedirect("/access-denied");
        }
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains("/api");
    }
}
