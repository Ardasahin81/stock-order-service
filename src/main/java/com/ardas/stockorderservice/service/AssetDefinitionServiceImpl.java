package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.repository.AssetDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssetDefinitionServiceImpl extends BaseServiceImpl<AssetDefinition, AssetDefinitionRepository> implements AssetDefinitionService {

    public AssetDefinitionServiceImpl(AssetDefinitionRepository repository) {
        super(repository);
    }

    @Override
    public AssetDefinition getOrCreate(String assetName) {
        return getRepository().findByName(assetName).orElseGet(() -> {
            AssetDefinition assetDefinition = new AssetDefinition();
            assetDefinition.setName(assetName);
            save(assetDefinition);
            return assetDefinition;
        });
    }

}
