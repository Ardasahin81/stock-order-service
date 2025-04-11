package com.ardas.stockorderservice.repository;

import com.ardas.stockorderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order> {

    List<Order> findByCustomerIdAndCreatedDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);

}
