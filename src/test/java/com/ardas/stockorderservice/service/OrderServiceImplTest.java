package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.dto.request.OrderCancelRequest;
import com.ardas.stockorderservice.dto.request.OrderCreateRequest;
import com.ardas.stockorderservice.dto.request.OrderMatchRequest;
import com.ardas.stockorderservice.model.Asset;
import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.model.Order;
import com.ardas.stockorderservice.model.enums.OrderStatus;
import com.ardas.stockorderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ardas.stockorderservice.model.enums.OrderSide.BUY;
import static com.ardas.stockorderservice.model.enums.OrderSide.SELL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderRepository repository;

    @Mock
    AssetService assetService;

    @Mock
    CustomerService customerService;

    @Mock
    AssetDefinitionService assetDefinitionService;

    @InjectMocks
    OrderServiceImpl orderService;

    @Captor
    ArgumentCaptor<Order> orderArgumentCaptor;

    @Test
    void testFindBy() {
        doReturn(List.of(createOrder())).when(repository).findByCustomerIdAndCreatedDateBetween(eq(1L), any(), any());

        orderService.findByDateRange(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertEquals(1, Mockito.mockingDetails(repository).getInvocations().size());
    }

    @Test
    void testCreateOrder() {

        doReturn(Optional.of(createCustomer())).when(customerService).get(1L);
        doReturn(createBaseAssetDefinition()).when(assetDefinitionService).getOrCreate("SISE");

        orderService.createOrder(createOrderCreateRequest());

        assertEquals(1, Mockito.mockingDetails(assetService).getInvocations().size());
        assertEquals(1, Mockito.mockingDetails(repository).getInvocations().size());
        verify(assetService, times(1)).reduceUsableAmount(1L, "TRY", new BigDecimal("72.8"));
        verify(repository, times(1)).save(orderArgumentCaptor.capture());

        Order captured = orderArgumentCaptor.getValue();

        assertEquals(1L, captured.getCustomer().getId());
        assertEquals(BUY, captured.getOrderSide());
        assertEquals("SISE", captured.getAsset().getName());
        assertEquals(new BigDecimal("4"), captured.getSize());
        assertEquals(new BigDecimal("18.2"), captured.getPrice());
        assertEquals(OrderStatus.PENDING, captured.getStatus());

    }

    @Test
    void testCancelOrder() {

        doReturn(Optional.of(createOrder())).when(repository).findById(142L);

        OrderCancelRequest request = new OrderCancelRequest();
        request.setOrderId(142L);

        orderService.cancelOrder(request);

        assertEquals(1, Mockito.mockingDetails(assetService).getInvocations().size());
        assertEquals(2, Mockito.mockingDetails(repository).getInvocations().size());
        verify(assetService, times(1)).increaseUsableAmount(1L, "SISE", new BigDecimal("19"));
        verify(repository, times(1)).findById(142L);
        verify(repository, times(1)).save(orderArgumentCaptor.capture());

        Order captured = orderArgumentCaptor.getValue();

        assertEquals(1L, captured.getCustomer().getId());
        assertEquals(SELL, captured.getOrderSide());
        assertEquals("SISE", captured.getAsset().getName());
        assertEquals(new BigDecimal("19"), captured.getSize());
        assertEquals(new BigDecimal("15.78"), captured.getPrice());
        assertEquals(OrderStatus.CANCELLED, captured.getStatus());

    }

    @Test
    void testNotCancellable() {

        Order mockOrder= createOrder();
        mockOrder.setStatus(OrderStatus.MATCHED);

        doReturn(Optional.of(mockOrder)).when(repository).findById(142L);

        OrderCancelRequest request = new OrderCancelRequest();
        request.setOrderId(142L);

        assertThrows(UnsupportedOperationException.class, () -> orderService.cancelOrder(request));

        assertEquals(0, Mockito.mockingDetails(assetService).getInvocations().size());
        assertEquals(1, Mockito.mockingDetails(repository).getInvocations().size());
        verify(repository, times(1)).findById(142L);

    }

    @Test
    void testMatch() {

        doReturn(Optional.of(createOrder())).when(repository).findById(142L);

        OrderMatchRequest request = new OrderMatchRequest();
        request.setOrderId(142L);

        orderService.matchOrder(request);

        assertEquals(1, Mockito.mockingDetails(assetService).getInvocations().size());
        assertEquals(2, Mockito.mockingDetails(repository).getInvocations().size());
        verify(assetService, times(1)).trade(1L, SELL, "SISE", new BigDecimal("15.78"), new BigDecimal("19"));
        verify(repository, times(1)).findById(142L);
        verify(repository, times(1)).save(orderArgumentCaptor.capture());

        Order captured = orderArgumentCaptor.getValue();

        assertEquals(1L, captured.getCustomer().getId());
        assertEquals(SELL, captured.getOrderSide());
        assertEquals("SISE", captured.getAsset().getName());
        assertEquals(new BigDecimal("19"), captured.getSize());
        assertEquals(new BigDecimal("15.78"), captured.getPrice());
        assertEquals(OrderStatus.MATCHED, captured.getStatus());

    }

    @Test
    void testNotMatchable() {

        Order mockOrder= createOrder();
        mockOrder.setStatus(OrderStatus.CANCELLED);

        doReturn(Optional.of(mockOrder)).when(repository).findById(142L);

        OrderMatchRequest request = new OrderMatchRequest();
        request.setOrderId(142L);

        assertThrows(UnsupportedOperationException.class, () -> orderService.matchOrder(request));

        assertEquals(0, Mockito.mockingDetails(assetService).getInvocations().size());
        assertEquals(1, Mockito.mockingDetails(repository).getInvocations().size());
        verify(repository, times(1)).findById(142L);

    }

    private Order createOrder() {
        Order order = new Order();
        order.setId(142L);
        order.setOrderSide(SELL);
        order.setStatus(OrderStatus.PENDING);
        order.setPrice(new BigDecimal("15.78"));
        order.setSize(new BigDecimal("19"));
        order.setAsset(createBaseAssetDefinition());
        order.setCustomer(createCustomer());
        return order;
    }

    private OrderCreateRequest createOrderCreateRequest() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setSide(BUY);
        request.setCustomerId(1L);
        request.setPrice(new BigDecimal("18.2"));
        request.setAssetName("SISE");
        request.setSize(new BigDecimal("4"));
        return request;
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