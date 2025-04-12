package com.ardas.stockorderservice.mapper;

import com.ardas.stockorderservice.dto.CustomerDto;
import com.ardas.stockorderservice.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public abstract class CustomerMapper {

    public static final CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    public abstract CustomerDto toCustomerDto(Customer customer);

    public abstract List<CustomerDto> toCustomerDto(Collection<Customer> customer);

}
