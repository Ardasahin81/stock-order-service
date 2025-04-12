package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.dto.request.OrderCancelRequest;
import com.ardas.stockorderservice.dto.request.OrderCreateRequest;
import com.ardas.stockorderservice.dto.request.OrderMatchRequest;
import com.ardas.stockorderservice.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends BaseService<Order> {

    List<Order> findByDateRange(Long customerId, LocalDateTime start, LocalDateTime end);

    Order createOrder(OrderCreateRequest request);

    void cancelOrder(OrderCancelRequest request);

    void matchOrder(OrderMatchRequest request);
}
