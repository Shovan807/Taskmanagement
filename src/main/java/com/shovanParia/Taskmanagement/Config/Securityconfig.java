package com.shovanParia.Taskmanagement.Config;

import com.shovanParia.Taskmanagement.JwtFilter.JwtFillter;
import com.shovanParia.Taskmanagement.Service.CoustomerDetailsSerivice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Securityconfig {

    private final CoustomerDetailsSerivice userDetailsService;
    private final JwtFillter jwtFillter;

    public Securityconfig(
            CoustomerDetailsSerivice userDetailsService,
            JwtFillter jwtFillter) {

        this.userDetailsService = userDetailsService;
        this.jwtFillter = jwtFillter;
    }


    // =========================
    // PASSWORD ENCODER
    // =========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // =========================
    // AUTHENTICATION PROVIDER
    // =========================
    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }


    // =========================
    // AUTHENTICATION MANAGER
    // =========================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


    // =========================
    // SECURITY FILTER CHAIN
    // =========================
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Disable CSRF for REST API
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Login does NOT require JWT
                        .requestMatchers(
                                "/public/**",
                                "/user/login")
                        .permitAll()

                        // Every other endpoint requires authentication
                        .anyRequest()
                        .authenticated()
                )

                // JWT API should be stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // JWT filter runs before username/password filter
                .addFilterBefore(
                        jwtFillter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}