package com.sergio.bank.util;

public class MessageConstants {
    private MessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    public static final String ERROR_EXIST_EMAIL = "El correo electrónico ya está registrado";
    public static final String ERROR_CUSTOMER_NOT_FOUND = "Customer not found with id: ";
    public static final String ACCOUNT_TYPE_SAVINGS = "SAVINGS";
    public static final String ACCOUNT_TYPE_CHECKING = "CHECKING";
    public static final String ERROR_INVALID_ACCOUNT_TYPE = "Invalid account type: ";
    public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found for ID: %d";
    public static final String ERROR_ACCOUNTS_NOT_FOUND_BY_CUSTOMER = "No se encontraron cuentas asociadas al cliente con el id: ";

}
