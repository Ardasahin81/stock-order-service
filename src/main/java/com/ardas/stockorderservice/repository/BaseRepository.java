package com.ardas.stockorderservice.repository;

import com.ardas.stockorderservice.model.Base;
import org.springframework.data.repository.CrudRepository;

public interface BaseRepository<T extends Base> extends CrudRepository<T, Long> {

}
