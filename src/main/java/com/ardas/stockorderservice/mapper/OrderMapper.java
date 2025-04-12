package com.ardas.stockorderservice.mapper;

import com.ardas.stockorderservice.dto.OrderDto;
import com.ardas.stockorderservice.dto.request.OrderCreateRequest;
import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.model.Order;
import com.ardas.stockorderservice.model.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Mapper
public abstract class OrderMapper {

    public static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    public abstract OrderDto toOrderDto(Order order);

    public abstract List<OrderDto> toOrderDto(Collection<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "orderSide", source = "request.side")
    @Mapping(target = "status", expression = "java(getInitialStatus())")
    public abstract Order toOrder(OrderCreateRequest request, Customer customer, AssetDefinition asset);

    protected OrderStatus getInitialStatus() {
        return OrderStatus.PENDING;
    }

    protected String toPlainString(BigDecimal number) {
        return number.toPlainString();
    }

}
