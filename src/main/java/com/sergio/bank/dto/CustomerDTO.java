package com.sergio.bank.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CustomerDTO {

    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "El correo no tiene un formato válido")
    private String email;

    @NotNull(message = "El número de documento no puede ser nulo")
    @NotEmpty(message = "El número de documento no puede estar vacío")
    private String documentNumber;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    public CustomerDTO(String name, String email, String documentNumber, String password) {
        this.name = name;
        this.email = email;
        this.documentNumber = documentNumber;
        this.password = password;
    }

    public CustomerDTO() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
