package com.sergio.bank.controller;

import com.sergio.bank.dto.AuthResponseDto;
import com.sergio.bank.dto.CustomerDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerWebMvcTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @BeforeEach
    public void setUp() {
        String customerJson = "{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"documentNumber\": \"1234567891\", \"password\": \"securepassword\"}";
        HttpHeaders customerHeaders = new HttpHeaders();
        customerHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> customerEntity = new HttpEntity<>(customerJson, customerHeaders);

        ResponseEntity<CustomerDTO> customerResponse = restTemplate.exchange("/api/customers", HttpMethod.POST, customerEntity, CustomerDTO.class);

        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        assertNotNull(Objects.requireNonNull(customerResponse.getBody()).getId());
        assertEquals("John Doe", customerResponse.getBody().getName());

        String loginJson = "{\"email\": \"john.doe@example.com\", \"pass\": \"securepassword\"}";
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginEntity = new HttpEntity<>(loginJson, loginHeaders);

        ResponseEntity<AuthResponseDto> loginResponse = restTemplate.exchange("/api/auth/login", HttpMethod.POST, loginEntity, AuthResponseDto.class);
        token = Objects.requireNonNull(loginResponse.getBody()).getToken();
    }

    @Test
    @Order(1)
    public void testGetCustomer() {
        long customerId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO> response = restTemplate.exchange("/api/customers/" + customerId, HttpMethod.GET, entity, CustomerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().getId());
    }

    @Test
    @Order(2)
    public void testUpdateCustomer() {
        long customerId = 1L;
        String updateCustomerJson = "{\"name\": \"Sergio Ayala\", \"email\": \"john.doe@example.com\", \"documentNumber\": \"987654321\", \"password\": \"newsecurepassword\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(updateCustomerJson, headers);

        ResponseEntity<CustomerDTO> response = restTemplate.exchange("/api/customers/" + customerId, HttpMethod.PUT, entity, CustomerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sergio Ayala", Objects.requireNonNull(response.getBody()).getName());
        assertEquals("987654321", response.getBody().getDocumentNumber());
    }

    @Test
    @Order(3)
    public void testDeleteCustomer() {
        long customerId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/customers/" + customerId, HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cliente eliminado exitosamente", response.getBody());
    }
}

