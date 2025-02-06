package com.sergio.bank.controller;

import com.sergio.bank.config.JwtUtil;
import com.sergio.bank.dto.AuthResponseDto;
import com.sergio.bank.dto.LoginDto;
import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private AuthController authController;
    private JwtUtil jwtUtil;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        customerRepository = mock(CustomerRepository.class);
        authController = new AuthController(jwtUtil, customerRepository);
    }


    @Test
    void testLoginUserNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("notfound@example.com");
        loginDto.setPass("password");

        when(customerRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authController.login(loginDto));
    }

    @Test
    void testLoginBadCredentials() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPass("wrongpassword");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test User");
        customer.setPassword("123456789");
        customer.setEmail("test@example.com");

        when(customerRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(customer));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authController.login(loginDto));
    }
}
