package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.Base;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends Base> {

    Optional<T> get(Long id);

    List<T> getAll();

    void save(T entity);

}
