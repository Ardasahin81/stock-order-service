package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.exception.InsufficientBalanceException;
import com.ardas.stockorderservice.model.Asset;
import com.ardas.stockorderservice.model.AssetDefinition;
import com.ardas.stockorderservice.model.Customer;
import com.ardas.stockorderservice.model.enums.OrderSide;
import com.ardas.stockorderservice.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.ardas.stockorderservice.model.enums.OrderSide.BUY;

@Service
@Transactional
public class AssetServiceImpl extends BaseServiceImpl<Asset, AssetRepository> implements AssetService {

    public static final String DEFAULT_QUOTE_ASSET = "TRY";
    public static final int DEFAULT_SCALE = 8;

    private final AssetDefinitionService assetDefinitionService;

    public AssetServiceImpl(AssetRepository repository, AssetDefinitionService assetDefinitionService) {
        super(repository);
        this.assetDefinitionService = assetDefinitionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> findByCustomerId(Long customerId) {
        return getRepository().findByCustomerId(customerId);
    }

    @Override
    public Asset getByCustomerAndAsset(Long customerId, String assetName) {

        List<Asset> assets = findByCustomerId(customerId);

        return assets.stream()
                .filter(a -> assetName.equals(a.getAssetDefinition().getName()))
                .findFirst()
                .orElseGet(() -> createNew(customerId, assetDefinitionService.getOrCreate(assetName)));

    }

    @Override
    public void reduceUsableAmount(Long customerId, String assetName, BigDecimal size) {
        Asset asset = getByCustomerAndAsset(customerId, assetName);
        sizeModifyHelper(asset, size, Asset::setUsableSize, Asset::getUsableSize, false);
        save(asset);
    }

    @Override
    public void increaseUsableAmount(Long customerId, String assetName, BigDecimal size) {
        Asset asset = getByCustomerAndAsset(customerId, assetName);
        sizeModifyHelper(asset, size, Asset::setUsableSize, Asset::getUsableSize, true);
        save(asset);
    }

    @Override
    public void trade(Long customerId, OrderSide side, String baseAssetName, BigDecimal price, BigDecimal size) {

        BigDecimal totalQuote = price.multiply(size);

        if (side == BUY) {
            actualizeOrderReduce(customerId, DEFAULT_QUOTE_ASSET, totalQuote);
            actualizeOrderIncrease(customerId, baseAssetName, size);
        } else {
            actualizeOrderIncrease(customerId, DEFAULT_QUOTE_ASSET, totalQuote);
            actualizeOrderReduce(customerId, baseAssetName, size);
        }
    }

    private void actualizeOrderReduce(Long customerId, String assetName, BigDecimal size) {
        Asset asset = getByCustomerAndAsset(customerId, assetName);
        sizeModifyHelper(asset, size, Asset::setSize, Asset::getSize, false);
        save(asset);
    }

    private void actualizeOrderIncrease(Long customerId, String assetName, BigDecimal size) {
        Asset asset = getByCustomerAndAsset(customerId, assetName);
        sizeModifyHelper(asset, size, Asset::setSize, Asset::getSize, true);
        sizeModifyHelper(asset, size, Asset::setUsableSize, Asset::getUsableSize, true);
        save(asset);
    }

    private void sizeModifyHelper(Asset asset,
                                  BigDecimal size,
                                  BiConsumer<Asset, BigDecimal> sizeFieldSetter,
                                  Function<Asset, BigDecimal> sizeFieldGetter,
                                  boolean increase) {

        BinaryOperator<BigDecimal> operator = increase ? BigDecimal::add : BigDecimal::subtract;

        BigDecimal scaledSize = size.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
        BigDecimal newSize = operator.apply(sizeFieldGetter.apply(asset), scaledSize);

        if (newSize.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(asset.getAssetDefinition().getName(), size, asset.getUsableSize());
        }

        sizeFieldSetter.accept(asset, newSize);

        if (asset.getSize().compareTo(asset.getUsableSize()) < 0) {
            throw new InsufficientBalanceException(asset.getAssetDefinition().getName(), asset.getUsableSize(), asset.getSize());
        }

    }

    private Asset createNew(Long customerId, AssetDefinition assetDefinition) {

        Customer customer = new Customer();
        customer.setId(customerId);

        Asset asset = new Asset();

        asset.setCustomer(customer);
        asset.setSize(BigDecimal.ZERO);
        asset.setUsableSize(BigDecimal.ZERO);
        asset.setAssetDefinition(assetDefinition);

        save(asset);
        return asset;
    }

}
