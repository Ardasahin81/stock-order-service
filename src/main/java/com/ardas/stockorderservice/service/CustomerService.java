package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.dto.request.CustomerCreateRequest;
import com.ardas.stockorderservice.model.Customer;

public interface CustomerService extends BaseService<Customer> {

    Customer createCustomer(CustomerCreateRequest request);

}
