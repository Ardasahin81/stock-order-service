package com.ardas.stockorderservice.mapper;

import com.ardas.stockorderservice.dto.OrderDto;
import com.ardas.stockorderservice.dto.request.OrderCreateRequest;
import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.model.Order;
import com.ardas.stockorderservice.model.enums.OrderSide;
import com.ardas.stockorderservice.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderMapperTest {

    @Test
    void testToDto() {
        Order order = new Order();
        order.setId(167L);
        order.setCreatedDate(LocalDateTime.of(2025, 2, 5, 10, 15, 15, 15));
        order.setModifiedDate(LocalDateTime.of(2025, 2, 5, 10, 15, 16, 15));
        order.setCustomer(createCustomer());
        order.setStatus(OrderStatus.PENDING);
        order.setAsset(createBaseAssetDefinition());
        order.setPrice(new BigDecimal("16"));
        order.setSize(new BigDecimal("11"));
        order.setOrderSide(OrderSide.BUY);

        OrderDto dto = OrderMapper.INSTANCE.toOrderDto(order);

        assertEquals(167L, order.getId());
        assertEquals("Arda", dto.getCustomer().getName());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 15, 15, 15), dto.getCreatedDate());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 15, 16, 15), dto.getModifiedDate());
        assertEquals(OrderStatus.PENDING, dto.getStatus());
        assertEquals("SISE", dto.getAsset().getName());
        assertEquals("16", dto.getPrice());
        assertEquals("11", dto.getSize());
        assertEquals(OrderSide.BUY, dto.getOrderSide());

    }

    @Test
    void testToDtoList() {
        Order order = new Order();
        order.setId(167L);
        order.setCreatedDate(LocalDateTime.of(2025, 2, 5, 10, 15, 15, 15));
        order.setModifiedDate(LocalDateTime.of(2025, 2, 5, 10, 15, 16, 15));
        order.setCustomer(createCustomer());
        order.setStatus(OrderStatus.PENDING);
        order.setAsset(createBaseAssetDefinition());
        order.setPrice(new BigDecimal("16"));
        order.setSize(new BigDecimal("11"));
        order.setOrderSide(OrderSide.BUY);

        Order order2 = new Order();
        order2.setId(168L);
        order2.setCreatedDate(LocalDateTime.of(2025, 2, 5, 10, 15, 20, 15));
        order2.setModifiedDate(LocalDateTime.of(2025, 2, 5, 10, 15, 21, 15));
        order2.setCustomer(createCustomer());
        order2.setStatus(OrderStatus.PENDING);
        order2.setAsset(createBaseAssetDefinition());
        order2.setPrice(new BigDecimal("14"));
        order2.setSize(new BigDecimal("13"));
        order2.setOrderSide(OrderSide.BUY);

        List<OrderDto> orderDto = OrderMapper.INSTANCE.toOrderDto(List.of(order, order2));

        assertEquals(167L, order.getId());
        assertEquals("Arda", orderDto.get(0).getCustomer().getName());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 15, 15, 15), orderDto.get(0).getCreatedDate());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 15, 16, 15), orderDto.get(0).getModifiedDate());
        assertEquals(OrderStatus.PENDING, orderDto.get(0).getStatus());
        assertEquals("SISE", orderDto.get(0).getAsset().getName());
        assertEquals("16", orderDto.get(0).getPrice());
        assertEquals("11", orderDto.get(0).getSize());
        assertEquals(OrderSide.BUY, orderDto.get(0).getOrderSide());

        assertEquals(167L, order.getId());
        assertEquals("Arda", orderDto.get(1).getCustomer().getName());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 15, 20, 15), orderDto.get(1).getCreatedDate());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 15, 21, 15), orderDto.get(1).getModifiedDate());
        assertEquals(OrderStatus.PENDING, orderDto.get(1).getStatus());
        assertEquals("SISE", orderDto.get(1).getAsset().getName());
        assertEquals("14", orderDto.get(1).getPrice());
        assertEquals("13", orderDto.get(1).getSize());
        assertEquals(OrderSide.BUY, orderDto.get(1).getOrderSide());
    }

    @Test
    void testToOrder() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setAssetName("SISE");
        request.setPrice(new BigDecimal("17.56"));
        request.setSide(OrderSide.SELL);
        request.setCustomerId(1L);
        request.setSize(new BigDecimal("4"));

        Order order = OrderMapper.INSTANCE.createOrder(request, createCustomer(), createBaseAssetDefinition());

        assertNull(order.getId());
        assertNull(order.getCreatedDate());
        assertNull(order.getModifiedDate());
        assertEquals("Arda", order.getCustomer().getName());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals("SISE", order.getAsset().getName());
        assertEquals(new BigDecimal("17.56"), order.getPrice());
        assertEquals(new BigDecimal("4"), order.getSize());
        assertEquals(OrderSide.SELL, order.getOrderSide());

    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("Arda");
        customer.setId(1L);
        return customer;
    }

    private AssetDefinition createBaseAssetDefinition() {
        AssetDefinition assetDefinition = new AssetDefinition();
        assetDefinition.setName("SISE");
        assetDefinition.setId(2L);
        return assetDefinition;
    }

}