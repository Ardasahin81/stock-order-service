package com.ardas.stockorderservice.exception;

import com.ardas.stockorderservice.model.Base;
import lombok.Getter;

import java.util.Map;

@Getter
public class RecordNotFoundException extends BaseException {

    private final Class<? extends Base> entityClass;
    private final Long id;

    public RecordNotFoundException(Class<? extends Base> entityClass, Long id) {
        super();
        this.entityClass = entityClass;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return entityClass.getSimpleName() + " with id " + id + " not found";
    }

    @Override
    public Map<String, Object> getDetails() {
        return Map.of("entity", entityClass.getSimpleName(), "id", id);
    }

    @Override
    public String getCode() {
        return "STOS101";
    }

}
