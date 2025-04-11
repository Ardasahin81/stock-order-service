package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.AssetDefinition;

public interface AssetDefinitionService extends BaseService<AssetDefinition> {

    AssetDefinition getOrCreate(String assetName);

}
