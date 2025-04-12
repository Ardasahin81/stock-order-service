package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.dto.request.CustomerCreateRequest;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl extends BaseServiceImpl<Customer, CustomerRepository> implements CustomerService {

    public CustomerServiceImpl(CustomerRepository repository) {
        super(repository);
    }

    @Override
    public Customer createCustomer(CustomerCreateRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        save(customer);
        return customer;
    }
}
