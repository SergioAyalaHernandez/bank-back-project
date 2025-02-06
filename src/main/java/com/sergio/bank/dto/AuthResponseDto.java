package com.sergio.bank.dto;

public class AuthResponseDto {
    private String token;
    private String email;
    private Long userId;
    private String userName;
    private String documentNumber;

    public AuthResponseDto(String token, String email, Long userId, String userName, String documentNumber) {
        this.token = token;
        this.email = email;
        this.userId = userId;
        this.userName = userName;
        this.documentNumber = documentNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

}
