package com.ardas.stockorderservice.mapper;

import com.ardas.stockorderservice.dto.AssetDefinitionDto;
import com.ardas.stockorderservice.dto.AssetDto;
import com.ardas.stockorderservice.model.Asset;
import com.ardas.stockorderservice.model.AssetDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Mapper
public abstract class AssetMapper {

    public static final AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    public abstract AssetDto toAssetDto(Asset asset);

    public abstract AssetDefinitionDto toAssetDefinitionDto(AssetDefinition assetDefinition);

    public abstract List<AssetDto> toAssetDto(Collection<Asset> assets);

    protected String toPlainString(BigDecimal number) {
        return number.toPlainString();
    }
}
