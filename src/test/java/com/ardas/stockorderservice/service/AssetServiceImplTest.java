package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.exception.InsufficientBalanceException;
import com.ardas.stockorderservice.model.Asset;
import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.model.enums.OrderSide;
import com.ardas.stockorderservice.repository.AssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    AssetDefinitionService assetDefinitionService;

    @Mock
    AssetRepository assetRepository;

    @InjectMocks
    AssetServiceImpl assetService;

    @Captor
    ArgumentCaptor<Asset> assetArgumentCaptor;

    @Test
    void testGetByCustomerAndAsset() {
        doReturn(List.of(createDefaultAsset())).when(assetRepository).findByCustomerId(any());
        doReturn(createBaseAssetDefinition()).when(assetDefinitionService).getOrCreate("SISE");

        Asset asset = assetService.getByCustomerAndAsset(1L, "TRY");

        assertEquals("TRY", asset.getAssetDefinition().getName());
        verify(assetDefinitionService, never()).getOrCreate(any());
        verify(assetRepository, never()).save(any());

        Asset nonExistingAsset = assetService.getByCustomerAndAsset(1L, "SISE");

        assertEquals("SISE", nonExistingAsset.getAssetDefinition().getName());
        verify(assetDefinitionService, times(1)).getOrCreate("SISE");
        verify(assetRepository, times(1)).save(any());
    }

    @Test
    void testReduceUsableAmount() {
        doReturn(List.of(createDefaultAsset())).when(assetRepository).findByCustomerId(any());
        assetService.reduceUsableAmount(1L, "TRY", new BigDecimal("5155.87"));

        verify(assetRepository, times(1)).save(assetArgumentCaptor.capture());

        assertEquals(new BigDecimal("4802.34000000"), assetArgumentCaptor.getValue().getUsableSize());
        assertEquals(new BigDecimal("10520.04"), assetArgumentCaptor.getValue().getSize());
    }

    @Test
    void testReduceUsableAmountInsufficient() {
        doReturn(List.of(createDefaultAsset())).when(assetRepository).findByCustomerId(any());

        BigDecimal amount = new BigDecimal("9958.22");

        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> assetService.reduceUsableAmount(1L, "TRY", amount)
        );

        verify(assetRepository, times(0)).save(any());

        assertEquals("TRY", exception.getDetails().get("asset"));
        assertEquals("9958.22", exception.getDetails().get("required"));
        assertEquals("9958.21", exception.getDetails().get("usable"));

    }

    @Test
    void testIncreaseUsableAmount() {
        doReturn(List.of(createDefaultAsset())).when(assetRepository).findByCustomerId(any());

        assetService.increaseUsableAmount(1L, "TRY", new BigDecimal("79.62"));

        verify(assetRepository, times(1)).save(assetArgumentCaptor.capture());

        assertEquals(new BigDecimal("10037.83000000"), assetArgumentCaptor.getValue().getUsableSize());
        assertEquals(new BigDecimal("10520.04"), assetArgumentCaptor.getValue().getSize());
    }

    @Test
    void testIncreaseUsableAmountExceed() {
        doReturn(List.of(createDefaultAsset())).when(assetRepository).findByCustomerId(any());

        BigDecimal amount = new BigDecimal("7900.62");

        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> assetService.increaseUsableAmount(1L, "TRY", amount)
        );

        verify(assetRepository, times(0)).save(any());

        assertEquals("TRY", exception.getDetails().get("asset"));
        assertEquals("17858.83000000", exception.getDetails().get("required"));
        assertEquals("10520.04", exception.getDetails().get("usable"));
    }

    @Test
    void testBuyTrade() {
        doReturn(List.of(createDefaultAsset())).when(assetRepository).findByCustomerId(any());
        doReturn(createBaseAssetDefinition()).when(assetDefinitionService).getOrCreate("SISE");

        assetService.trade(
                1L,
                OrderSide.BUY,
                "SISE",
                new BigDecimal("17.6"),
                new BigDecimal("5"));


        verify(assetRepository, times(3)).save(assetArgumentCaptor.capture());

        List<Asset> captured = assetArgumentCaptor.getAllValues();

        Asset quote = captured.stream().filter(a -> "TRY".equals(a.getAssetDefinition().getName())).findFirst().orElseThrow();
        Asset base = captured.stream().filter(a -> "SISE".equals(a.getAssetDefinition().getName())).findFirst().orElseThrow();

        assertEquals(new BigDecimal("10432.04000000"), quote.getSize());
        assertEquals(new BigDecimal("9958.21"), quote.getUsableSize());

        assertEquals(new BigDecimal("5.00000000"), base.getSize());
        assertEquals(new BigDecimal("5.00000000"), base.getUsableSize());

    }

    @Test
    void testSellTrade() {

        doReturn(List.of(createDefaultAsset(), createBaseAsset())).when(assetRepository).findByCustomerId(any());

        assetService.trade(
                1L,
                OrderSide.SELL,
                "SISE",
                new BigDecimal("17.6"),
                new BigDecimal("3"));


        verify(assetRepository, times(2)).save(assetArgumentCaptor.capture());

        List<Asset> captured = assetArgumentCaptor.getAllValues();

        Asset quote = captured.stream().filter(a -> "TRY".equals(a.getAssetDefinition().getName())).findFirst().orElseThrow();
        Asset base = captured.stream().filter(a -> "SISE".equals(a.getAssetDefinition().getName())).findFirst().orElseThrow();

        assertEquals(new BigDecimal("10572.84000000"), quote.getSize());
        assertEquals(new BigDecimal("10011.01000000"), quote.getUsableSize());

        assertEquals(new BigDecimal("2.00000000"), base.getSize());
        assertEquals(new BigDecimal("2"), base.getUsableSize());

    }

    private Asset createDefaultAsset() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setCustomer(createCustomer());
        asset.setAssetDefinition(createDefaultAssetDefinition());
        asset.setSize(new BigDecimal("10520.04"));
        asset.setUsableSize(new BigDecimal("9958.21"));
        return asset;
    }

    private Asset createBaseAsset() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setCustomer(createCustomer());
        asset.setAssetDefinition(createBaseAssetDefinition());
        asset.setSize(new BigDecimal("5"));
        asset.setUsableSize(new BigDecimal("2"));
        return asset;
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