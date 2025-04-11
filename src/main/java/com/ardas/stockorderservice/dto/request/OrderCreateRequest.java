package com.ardas.stockorderservice.dto.request;

import com.ardas.stockorderservice.model.enums.OrderSide;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateRequest {

    private Long customerId;
    private String assetName;
    private OrderSide side;
    private BigDecimal size;
    private BigDecimal price;


}
