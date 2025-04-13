package com.ardas.stockorderservice.web;

import com.ardas.stockorderservice.dto.OrderDto;
import com.ardas.stockorderservice.dto.request.OrderCancelRequest;
import com.ardas.stockorderservice.dto.request.OrderCreateRequest;
import com.ardas.stockorderservice.dto.request.OrderMatchRequest;
import com.ardas.stockorderservice.mapper.OrderMapper;
import com.ardas.stockorderservice.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final OrderMapper MAPPER = OrderMapper.INSTANCE;

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderDto create(@RequestBody OrderCreateRequest request) {
        return MAPPER.toOrderDto(orderService.createOrder(request));
    }

    @GetMapping("/list")
    public List<OrderDto> list(@RequestParam(required = false) Long customerId,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return MAPPER.toOrderDto(orderService.findByDateRange(customerId, start, end));
    }

    @DeleteMapping
    public void cancel(@RequestBody OrderCancelRequest request) {
        orderService.cancelOrder(request);
    }

    @PostMapping("/match")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void match(@RequestBody OrderMatchRequest request) {
        orderService.matchOrder(request);
    }

}
