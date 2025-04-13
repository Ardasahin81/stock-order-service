package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.Asset;
import com.ardas.stockorderservice.model.enums.OrderSide;

import java.math.BigDecimal;
import java.util.List;

public interface AssetService extends BaseService<Asset> {

    List<Asset> findByCustomerId(Long customerId);

    Asset getByCustomerAndAsset(Long customerId, String assetName);

    void reduceUsableAmount(Long customerId, String assetName, BigDecimal size);

    void increaseUsableAmount(Long customerId, String assetName, BigDecimal size);

    void trade(Long customerId, OrderSide side, String assetName, BigDecimal price, BigDecimal size);
}
