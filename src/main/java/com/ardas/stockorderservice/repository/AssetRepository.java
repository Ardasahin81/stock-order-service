package com.ardas.stockorderservice.repository;

import com.ardas.stockorderservice.model.Asset;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends BaseRepository<Asset>{

    List<Asset> findByCustomerId(Long customerId);

}
