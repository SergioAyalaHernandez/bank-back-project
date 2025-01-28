package com.sergio.bank.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "document_number", unique = true, nullable = false)
    private String documentNumber;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;



    public Customer(Customer existingCustomer) {
        this.id = existingCustomer.getId();
        this.name = existingCustomer.getName();
        this.email = existingCustomer.getEmail();
        this.password = existingCustomer.getPassword();
        this.accounts = new ArrayList<>(existingCustomer.getAccounts()); // Crea una nueva lista para evitar modificar la original
    }

    public Customer() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
