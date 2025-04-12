package com.ardas.stockorderservice.mapper;

import com.ardas.stockorderservice.dto.AssetDefinitionDto;
import com.ardas.stockorderservice.dto.AssetDto;
import com.ardas.stockorderservice.model.Asset;
import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.model.Customer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetMapperTest {

    @Test
    void testToDto() {
        Asset asset = new Asset();
        asset.setAssetDefinition(createDefaultAssetDefinition());
        asset.setCustomer(createCustomer());
        asset.setSize(new BigDecimal("1.502E2"));
        asset.setUsableSize(BigDecimal.ZERO);
        asset.setId(22L);

        AssetDto assetDto = AssetMapper.INSTANCE.toAssetDto(asset);

        assertEquals("TRY", assetDto.getAssetDefinition().getName());
        assertEquals("Arda", assetDto.getCustomer().getName());
        assertEquals("150.2", assetDto.getSize());
        assertEquals("0", assetDto.getUsableSize());
    }

    @Test
    void testToDtoList() {
        Asset quoteAsset = new Asset();
        quoteAsset.setAssetDefinition(createDefaultAssetDefinition());
        quoteAsset.setCustomer(createCustomer());
        quoteAsset.setSize(new BigDecimal("1.502E2"));
        quoteAsset.setUsableSize(BigDecimal.ZERO);
        quoteAsset.setId(22L);

        Asset baseAsset = new Asset();
        baseAsset.setAssetDefinition(createBaseAssetDefinition());
        baseAsset.setCustomer(createCustomer());
        baseAsset.setSize(new BigDecimal("7"));
        baseAsset.setUsableSize(new BigDecimal("4"));
        baseAsset.setId(22L);

        List<AssetDto> assetDto = AssetMapper.INSTANCE.toAssetDto(List.of(baseAsset, quoteAsset));

        assertEquals(2, assetDto.size());

        assertEquals("SISE", assetDto.get(0).getAssetDefinition().getName());
        assertEquals("Arda", assetDto.get(0).getCustomer().getName());
        assertEquals("7", assetDto.get(0).getSize());
        assertEquals("4", assetDto.get(0).getUsableSize());

        assertEquals("TRY", assetDto.get(1).getAssetDefinition().getName());
        assertEquals("Arda", assetDto.get(1).getCustomer().getName());
        assertEquals("150.2", assetDto.get(1).getSize());
        assertEquals("0", assetDto.get(1).getUsableSize());
    }

    @Test
    void testToAssertDefinitionDto() {
        AssetDefinitionDto dto = AssetMapper.INSTANCE.toAssetDefinitionDto(createBaseAssetDefinition());
        assertEquals("SISE", dto.getName());
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("Arda");
        customer.setId(1L);
        return customer;
    }

    private AssetDefinition createDefaultAssetDefinition() {
        AssetDefinition assetDefinition = new AssetDefinition();
        assetDefinition.setName("TRY");
        assetDefinition.setId(1L);
        return assetDefinition;
    }

    private AssetDefinition createBaseAssetDefinition() {
        AssetDefinition assetDefinition = new AssetDefinition();
        assetDefinition.setName("SISE");
        assetDefinition.setId(2L);
        return assetDefinition;
    }
}