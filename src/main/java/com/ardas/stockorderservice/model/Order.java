package com.ardas.stockorderservice.model;

import com.ardas.stockorderservice.model.enums.OrderSide;
import com.ardas.stockorderservice.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "s_order")
public class Order extends Base {

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "assetName", referencedColumnName = "name")
    private AssetDefinition asset;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;

    @Column(precision = 19, scale = 8)
    private BigDecimal size;

    @Column(precision = 19, scale = 8)
    private BigDecimal price;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
