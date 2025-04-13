package com.ardas.stockorderservice.exception;

import java.util.Map;

public record ErrorResponse(String code, String message, Map<String, Object> details) {

}
