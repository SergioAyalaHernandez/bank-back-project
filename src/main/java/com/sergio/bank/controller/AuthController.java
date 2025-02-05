package com.sergio.bank.controller;

import com.sergio.bank.config.JwtUtil;
import com.sergio.bank.dto.AuthResponseDto;
import com.sergio.bank.dto.LoginDto;
import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.CustomerRepository;
import com.sergio.bank.util.MessageConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final CustomerRepository personaRepository;

    public AuthController(JwtUtil jwtUtil, CustomerRepository personaRepository) {
        this.jwtUtil = jwtUtil;
        this.personaRepository = personaRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto dto) {
        try {
            Customer customer = personaRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException(MessageConstants.ERROR_USER_NOT_FOUND));

            String jwt = jwtUtil.create(dto.getEmail(), customer.getId());

            AuthResponseDto responseDto = new AuthResponseDto(
                    jwt,
                    dto.getEmail(),
                    customer.getId(),
                    customer.getName(),
                    customer.getDocumentNumber()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwt)
                    .body(responseDto);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException(MessageConstants.ERROR_INVALID_CREDENTIALS);
        }
    }
}
