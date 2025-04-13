package com.ardas.stockorderservice.repository;

import com.ardas.stockorderservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUsername(String username);

}