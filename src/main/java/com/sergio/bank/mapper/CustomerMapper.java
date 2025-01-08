package com.sergio.bank.mapper;

import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO dto);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CustomerDTO customerDTO, @MappingTarget Customer customer);
}
