package com.diadema.app_entrega_pizza_api.domain.model;


import com.diadema.app_entrega_pizza_api.domain.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "tb_usuarios")
@Entity(name = "tb_usuarios")
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Gera UUIDs automaticamente
    private UUID id; // Mudou de Long para UUID

    private String login;
    private String password;

    // --- MUDANÇA AQUI ---
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Agora a comparação é segura com Enum, não tem perigo de errar string
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getUsername() { return login; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}