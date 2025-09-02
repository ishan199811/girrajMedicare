package com.girrajmedico.girrajmedico.configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = "USER"; // Default to USER role

        // Check for ADMIN role in authorities
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                role = "ADMIN"; // Assign ADMIN role if found
                break;
            }
            if (authority.getAuthority().equals("DOCTOR")) {
                role = "DOCTOR"; // Assign DOCTOR role if found
                break;
            }
        }

        // Prepare the response data
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("status", "success");
        jsonResponse.put("username", authentication.getName());
        jsonResponse.put("role", role); // Include the role in the response

        // Set the response status and content type
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        
        // Write the JSON response to the output stream
        objectMapper.writeValue(response.getWriter(), jsonResponse);
    }
}
