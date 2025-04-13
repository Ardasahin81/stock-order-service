package com.ardas.stockorderservice.service;

import com.ardas.stockorderservice.model.User;
import com.ardas.stockorderservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends BaseServiceImpl<User, UserRepository> implements UserDetailsService {

    public UserService(UserRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return getRepository().findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
