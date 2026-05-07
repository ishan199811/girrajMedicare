package com.girrajmedico.girrajmedico.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.girrajmedico.girrajmedico.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebSecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public WebSecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // --- CRITICAL: Allows the browser's CORS preflight OPTIONS request ---
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 1. Put specific protected routes FIRST
                .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN", "DOCTOR")
                .requestMatchers("/doctor/**").hasAnyAuthority("DOCTOR", "ADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")

                // 2. Put specific public routes NEXT
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/css/**",
                    "/image/**",
                    "/music/**",
                    "/login",
                    "/registration",
                    "/property",
                    "/api/getAllProperty",
                    "/productlist",
                    "/service",
                    "/contact",
                    "/api/register",
                    "/api/docregister",
                    "/api/getAllMedicine",
                    "/test"
                ).permitAll()
                
                // 3. DO NOT put "/**" permitAll() here unless you want NO security.
                // .requestMatchers("/**").permitAll() // <-- REMOVE THIS

                // 4. Anything else must be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Fixed the double slash typo in your URL
                .loginPage("http://localhost:3000/login") 
                .loginProcessingUrl("/api/login")
                .successHandler(successHandler) 
                // CRITICAL: Stop Spring from doing a 302 redirect on failed login. Return 401 instead.
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Login Failed: Invalid credentials");
                })
                .permitAll()
            )
            // Add an entry point to return 401 instead of a 302 redirect for unauthenticated requests
            .exceptionHandling(eh -> eh
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Please log in");
                })
                .accessDeniedPage("/403")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                // Fixed to a RESTful logout response to prevent CORS redirect blocks
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Logged out successfully");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
             "https://68cfa656150fee57c3b2af0e--peaceful-manatee-07e3ef.netlify.app",
             "https://girrajmedicare.com", // Production frontend
             
             // --- EXPANDED LOCAL DEVELOPMENT ORIGINS FOR ROBUSTNESS ---
             // Removed trailing slash and added HTTPS/127.0.0.1 variants
             "http://localhost:3000",
             "https://localhost:3000", 
             "http://127.0.0.1:3000",
             "https://127.0.0.1:3000",
             // Add other local ports if necessary (e.g., Vite/Webpack dev servers)
             "http://localhost:5173",
             "https://localhost:5173" 
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers
        config.setAllowCredentials(true); // CRITICAL for "credentials: 'include'"

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
