package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.dto.request.OrderCancelRequest;
import com.ardas.stockorderservice.dto.request.OrderCreateRequest;
import com.ardas.stockorderservice.dto.request.OrderMatchRequest;
import com.ardas.stockorderservice.exception.RecordNotFoundException;
import com.ardas.stockorderservice.mapper.OrderMapper;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.model.Order;
import com.ardas.stockorderservice.model.enums.OrderStatus;
import com.ardas.stockorderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.ardas.stockorderservice.model.enums.OrderSide.BUY;
import static com.ardas.stockorderservice.service.AssetServiceImpl.DEFAULT_QUOTE_ASSET;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl extends BaseServiceImpl<Order, OrderRepository> implements OrderService {

    private static final OrderMapper MAPPER = OrderMapper.INSTANCE;

    private final AssetService assetService;
    private final CustomerService customerService;
    private final AssetDefinitionService assetDefinitionService;

    public OrderServiceImpl(OrderRepository repository, AssetService assetService, CustomerService customerService, AssetDefinitionService assetDefinitionService) {
        super(repository);
        this.assetService = assetService;
        this.customerService = customerService;
        this.assetDefinitionService = assetDefinitionService;
    }

    @Override
    public List<Order> findByDateRange(Long customerId, LocalDateTime start, LocalDateTime end) {
        return getRepository().findByCustomerIdAndCreatedDateBetween(customerId, start, end);
    }

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        Customer customer = customerService.get(request.getCustomerId())
                .orElseThrow(() -> new RecordNotFoundException(Customer.class, request.getCustomerId()));

        Order order = MAPPER.toOrder(request, customer, assetDefinitionService.getOrCreate(request.getAssetName()));

        assetService.reduceUsableAmount(customer.getId(), getRequiredAssetName(order), getRequiredAmount(order));

        save(order);
        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(OrderCancelRequest request) {
        Order order = get(request.getOrderId())
                .orElseThrow(() -> new RecordNotFoundException(Order.class, request.getOrderId()));

        if(!isModifiable(order)) {
            throw new UnsupportedOperationException("only pending orders can be cancelled");
        }

        assetService.increaseUsableAmount(order.getCustomer().getId(), getRequiredAssetName(order), getRequiredAmount(order));

        order.setStatus(OrderStatus.CANCELLED);
        save(order);
    }

    @Override
    @Transactional
    public void matchOrder(OrderMatchRequest request) {
        Order order = get(request.getOrderId())
                .orElseThrow(() -> new RecordNotFoundException(Order.class, request.getOrderId()));

        if(!isModifiable(order)) {
            throw new UnsupportedOperationException("only pending orders can be matched");
        }

        assetService.trade(
                order.getCustomer().getId(),
                order.getOrderSide(),
                order.getAsset().getName(),
                order.getPrice(),
                order.getSize()
        );

        order.setStatus(OrderStatus.MATCHED);
        save(order);
    }

    private boolean isModifiable(Order order) {
        return order.getStatus() == OrderStatus.PENDING;
    }

    private BigDecimal getRequiredAmount(Order order) {
        return order.getOrderSide() == BUY ? order.getSize().multiply(order.getPrice()) : order.getSize();
    }

    private String getRequiredAssetName(Order order) {
        return order.getOrderSide() == BUY ? DEFAULT_QUOTE_ASSET : order.getAsset().getName();
    }
}
