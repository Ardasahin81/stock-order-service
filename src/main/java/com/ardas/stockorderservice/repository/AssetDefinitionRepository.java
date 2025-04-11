package com.ardas.stockorderservice.repository;

import com.ardas.stockorderservice.model.AssetDefinition;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetDefinitionRepository extends BaseRepository<AssetDefinition> {

    Optional<AssetDefinition> findByName(String name);

}
