package com.sergio.bank.mapper;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.SavingsAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "type", expression = "java(getAccountType(account))")
    @Mapping(target = "customerId", source = "customerId")

    AccountDTO toDTO(Account account);

    default String getAccountType(Account account) {
        return account instanceof SavingsAccount ? "SAVINGS" : "CHECKING";
    }
}
