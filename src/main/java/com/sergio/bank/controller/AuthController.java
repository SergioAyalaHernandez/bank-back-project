package com.sergio.bank.controller;

import com.sergio.bank.config.JwtUtil;
import com.sergio.bank.dto.AuthResponseDto;
import com.sergio.bank.dto.LoginDto;
import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.CustomerRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomerRepository personaRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomerRepository personaRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.personaRepository = personaRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto dto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPass());
        Authentication authentication = this.authenticationManager.authenticate(login);
        String jwt = this.jwtUtil.create(dto.getEmail());
        Customer customer = personaRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        AuthResponseDto responseDto = new AuthResponseDto(jwt, dto.getEmail(), customer.getId());
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).body(responseDto);
    }
}
