package com.ardas.stockorderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "s_asset")
public class Asset extends Base {

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "assetName", referencedColumnName = "name")
    private AssetDefinition assetDefinition;

    @Column(precision = 19, scale = 8)
    private BigDecimal size;

    @Column(precision = 19, scale = 8)
    private BigDecimal usableSize;

}
