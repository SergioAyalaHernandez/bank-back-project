package com.sergio.bank.service.impl;

import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.BadRequestException;
import com.sergio.bank.exception.CustomerNotFoundException;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.CustomerRepository;
import com.sergio.bank.util.MessageConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerDTO customerDTO;
    private Customer customer;

    @Mock
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        // Configuración inicial para Customer y CustomerDTO
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setPassword("encodedPassword123");

        customerDTO = new CustomerDTO();
        customerDTO.setEmail("test@example.com");
        customerDTO.setPassword("encodedPassword123");
    }

    @Test
    void testCreateCustomer_EmailAlreadyExists() {
        // Arrange
        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(java.util.Optional.of(new Customer()));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            customerService.createCustomer(customerDTO);
        });
        assertEquals(MessageConstants.ERROR_EXIST_EMAIL, exception.getMessage());
    }

    @Test
    void testGetCustomer_Success() {
        // Arrange: Simulamos que el repositorio devuelve un cliente con ID 1
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        // Act: Llamamos al método getCustomer para obtener el cliente
        CustomerDTO result = customerService.getCustomer(1L);

        // Assert: Verificamos que el resultado no sea nulo y que coincidan los valores
        assertNotNull(result);
        assertEquals(customer.getEmail(), result.getEmail());
        assertEquals(customer.getPassword(), result.getPassword());

        // Verificar que se haya llamado al repositorio
        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetCustomer_NotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(1L));
    }

}
