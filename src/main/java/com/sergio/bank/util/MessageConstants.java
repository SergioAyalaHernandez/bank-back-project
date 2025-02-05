package com.sergio.bank.util;

public class MessageConstants {
    private MessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    public static final String ERROR_EXIST_EMAIL = "El correo electrónico ya está registrado";
    public static final String ERROR_USER_NOT_FOUND = "Usuario no encontrado";
    public static final String CLIENT_DELETED_SUCCESSFULLY  = "Cliente eliminado exitosamente";
    public static final String ERROR_CUSTOMER_NOT_FOUND = "Customer not found with id: ";
    public static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";
    public static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    public static final String TRANSACTION_TYPE_WITHDRAWAL = "WITHDRAWAL";
    public static final String ERROR_UNSUPPORTED_TRANSACTION_TYPE = "Unsupported transaction type";
    public static final String TRANSACTION_STATUS_SUCCESS = "SUCCESS";
    public static final String ACCOUNT_TYPE_SAVINGS = "SAVINGS";
    public static final String ACCOUNT_TYPE_CHECKING = "CHECKING";
    public static final String ERROR_INVALID_ACCOUNT_TYPE = "Invalid account type: ";
    public static final String ERROR_STRATEGY = "Transaction strategy not set";
    public static final String ERROR_INVALID_CREDENTIALS = "Credenciales invalidas";
    public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found for ID: %d";
}
