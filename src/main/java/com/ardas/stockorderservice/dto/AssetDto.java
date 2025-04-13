package com.ardas.stockorderservice.dto;

import lombok.Data;

@Data
public class AssetDto {

    private CustomerDto customer;
    private AssetDefinitionDto assetDefinition;
    private String size;
    private String usableSize;

}
