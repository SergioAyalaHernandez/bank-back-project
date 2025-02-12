package com.sergio.bank.controller;

import com.sergio.bank.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

}
