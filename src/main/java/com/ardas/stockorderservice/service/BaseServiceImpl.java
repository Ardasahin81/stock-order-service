package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.Base;
import com.ardas.stockorderservice.repository.BaseRepository;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class BaseServiceImpl<T extends Base, R extends BaseRepository<T>> implements BaseService<T> {

    private final R repository;

    public BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> get(Long id) {
        return getRepository().findById(id);
    }

    @Override
    public List<T> getAll() {
        return (List<T>) repository.findAll();
    }

    @Override
    public void save(T entity) {
        getRepository().save(entity);
    }

}
