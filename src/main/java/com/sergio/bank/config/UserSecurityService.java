package com.sergio.bank.config;

import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    private CustomerRepository customerRepository;

    @Autowired
    public UserSecurityService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe ese correo"));
        System.out.println(customer);
        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword()) // La contraseña ya debe estar encriptada en la base de datos
                .accountLocked(false)
                .disabled(false)
                .build();
    }

}
