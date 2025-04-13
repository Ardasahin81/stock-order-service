package com.ardas.stockorderservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "s_user")
public class User extends Base implements UserDetails {

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference
    private List<UserRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

}
