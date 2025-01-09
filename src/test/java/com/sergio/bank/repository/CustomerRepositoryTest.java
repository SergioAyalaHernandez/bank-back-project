package com.sergio.bank.repository;

import com.sergio.bank.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setDocumentNumber("1234567891");
        customer.setPassword("hashedPassword");
    }

    @Test
    void whenSave_thenReturnSavedCustomer() {
        Customer savedCustomer = customerRepository.save(customer);
        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals(customer.getName(), savedCustomer.getName());
        assertEquals(customer.getEmail(), savedCustomer.getEmail());
    }

    @Test
    void whenFindByEmail_thenReturnCustomer() {
        entityManager.persistAndFlush(customer);

        Optional<Customer> found = customerRepository.findByEmail(customer.getEmail());

        assertTrue(found.isPresent());
        assertEquals(customer.getEmail(), found.get().getEmail());
    }

    @Test
    void whenFindByInvalidEmail_thenReturnEmpty() {
        entityManager.persistAndFlush(customer);

        Optional<Customer> found = customerRepository.findByEmail("invalid@email.com");

        assertTrue(found.isEmpty());
    }

    @Test
    void whenDeleteCustomer_thenCustomerShouldNotExist() {
        Customer savedCustomer = entityManager.persistAndFlush(customer);

        customerRepository.deleteById(savedCustomer.getId());
        Optional<Customer> found = customerRepository.findById(savedCustomer.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void whenUpdateCustomer_thenReturnUpdatedCustomer() {
        Customer savedCustomer = entityManager.persistAndFlush(customer);

        savedCustomer.setName("Jane Doe");
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        assertEquals("Jane Doe", updatedCustomer.getName());
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());
    }

    @Test
    void whenFindByDocumentNumber_thenReturnCustomer() {
        entityManager.persistAndFlush(customer);

        Optional<Customer> found = customerRepository.findByDocumentNumber((customer.getDocumentNumber()));

        assertTrue(found.isPresent());
        assertEquals(customer.getDocumentNumber(), found.get().getDocumentNumber());
    }
}