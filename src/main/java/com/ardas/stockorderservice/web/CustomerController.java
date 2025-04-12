package com.ardas.stockorderservice.web;

import com.ardas.stockorderservice.dto.CustomerDto;
import com.ardas.stockorderservice.dto.request.CustomerCreateRequest;
import com.ardas.stockorderservice.mapper.CustomerMapper;
import com.ardas.stockorderservice.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerMapper MAPPER = CustomerMapper.INSTANCE;

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public CustomerDto createCustomer(@RequestBody CustomerCreateRequest request) {
        return MAPPER.toCustomerDto(customerService.createCustomer(request));
    }
}
