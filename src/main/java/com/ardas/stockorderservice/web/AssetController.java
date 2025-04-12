package com.ardas.stockorderservice.web;

import com.ardas.stockorderservice.dto.AssetDto;
import com.ardas.stockorderservice.mapper.AssetMapper;
import com.ardas.stockorderservice.service.AssetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    private static final AssetMapper MAPPER = AssetMapper.INSTANCE;

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/list/{customerId}")
    public List<AssetDto> list(@PathVariable Long customerId) {
        return MAPPER.toAssetDto(assetService.findByCustomerId(customerId));
    }
}
