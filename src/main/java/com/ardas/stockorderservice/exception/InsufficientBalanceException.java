package com.ardas.stockorderservice.exception;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class InsufficientBalanceException extends BaseException {

    private final String assetName;
    private final BigDecimal required;
    private final BigDecimal balance;

    public InsufficientBalanceException(String assetName, BigDecimal required, BigDecimal balance) {
        super();
        this.assetName = assetName;
        this.required = required;
        this.balance = balance;
    }

    @Override
    public String getMessage() {
        return "Insufficient " + assetName + " balance. Required: " + required.toPlainString() + ", balance: " + balance.toPlainString();
    }

    @Override
    public Map<String, Object> getDetails() {
        return Map.of("asset", assetName, "required", required.toPlainString() , "usable", balance.toPlainString());
    }

    @Override
    public String getCode() {
        return "STOS102";
    }

}
