package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.dto.request.CustomerCreateRequest;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    CustomerRepository repository;

    @InjectMocks
    CustomerServiceImpl customerService;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @Test
    void testCreateCustomer() {
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setName("Arda");
        Customer customer = customerService.createCustomer(request);

        verify(repository, times(1)).save(customerArgumentCaptor.capture());

        assertEquals("Arda", customer.getName());
        assertEquals("Arda", customerArgumentCaptor.getValue().getName());
    }
}