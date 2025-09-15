package com.girrajmedico.girrajmedico.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                    "/**"
                    
                    
                      ).permitAll()
                .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN","DOCTOR")
                .requestMatchers("/doctor/**").hasAnyAuthority("DOCTOR", "ADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
)
            .formLogin(form -> form
                .loginPage("http://girrajmedicare.com/login")  //http://localhost:3000/login
                .loginProcessingUrl("/api/login")
                .successHandler(successHandler) // Your existing login success handler
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("http://girrajmedicare.com/") // Redirect to login page after successful logout
                .invalidateHttpSession(true)  // Invalidate the session after logout
                .clearAuthentication(true)  // Clear authentication on logout
                .permitAll()
            )
            .exceptionHandling(eh -> eh.accessDeniedPage("/403"));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://girrajmedicare.com")); // Allow frontend domain
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        config.setAllowCredentials(true); // Allow credentials (cookies, auth headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
