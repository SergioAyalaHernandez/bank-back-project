package com.sergio.bank.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        // Para acceder a la consola de la h2
                        .requestMatchers("/h2-console/**","/swagger-ui/**","/v3/api-docs/swagger-config","/v3/api-docs","/swagger-ui.html").permitAll()
                        .requestMatchers("/error", "/favicon.ico","/favicon-32x32.png").permitAll()
                        // Endpoints específicos para customers
                        .requestMatchers(HttpMethod.POST, "/api/customers/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/api/accounts/**").permitAll()
                        // Endpoints específicos para accounts
                        .requestMatchers(HttpMethod.POST, "/api/accounts").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/accounts/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**", "/api/**","/h2-console/**","/swagger-ui.html", "/swagger-ui/**","/v3/api-docs/swagger-config","/v3/api-docs")
                )
                .headers(headers -> headers
                        .frameOptions().disable()

                )
                .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


