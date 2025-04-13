package com.ardas.stockorderservice.dto;

import com.ardas.stockorderservice.model.enums.OrderSide;
import com.ardas.stockorderservice.model.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private CustomerDto customer;
    private AssetDefinitionDto asset;
    private OrderSide orderSide;
    private String size;
    private String price;
    private OrderStatus status;

}
