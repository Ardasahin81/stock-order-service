package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.repository.AssetDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssetDefinitionServiceImplTest {

    AssetDefinitionRepository repository;
    AssetDefinitionServiceImpl assetDefinitionService;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(AssetDefinitionRepository.class);
        assetDefinitionService = new AssetDefinitionServiceImpl(repository);
    }

    @Test
    void testFound() {
        doReturn(Optional.of(createDefaultAssetDefinition())).when(repository).findByName("TRY");

        AssetDefinition asset = assetDefinitionService.getOrCreate("TRY");

        assertEquals("TRY", asset.getName());

        verify(repository, times(1)).findByName(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void testNotFound() {
        doReturn(Optional.empty()).when(repository).findByName("TRY");

        AssetDefinition asset = assetDefinitionService.getOrCreate("TRY");

        assertEquals("TRY", asset.getName());

        verify(repository, times(1)).findByName(anyString());
        verify(repository, times(1)).save(any());
    }

    private AssetDefinition createDefaultAssetDefinition() {
        AssetDefinition assetDefinition = new AssetDefinition();
        assetDefinition.setName("TRY");
        assetDefinition.setId(1L);
        return assetDefinition;
    }
}