package com.sergio.bank.mapper;

import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO dto);
}
