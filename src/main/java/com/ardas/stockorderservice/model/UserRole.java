package com.ardas.stockorderservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "s_user_role")
public class UserRole extends Base implements GrantedAuthority {

    @Column
    private String role;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

    @Override
    public String getAuthority() {
        return role;
    }

}
