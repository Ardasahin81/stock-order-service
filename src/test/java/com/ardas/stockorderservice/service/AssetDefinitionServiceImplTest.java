package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.repository.AssetDefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetDefinitionServiceImplTest {

    @Mock
    AssetDefinitionRepository repository;

    @InjectMocks
    AssetDefinitionServiceImpl assetDefinitionService;

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