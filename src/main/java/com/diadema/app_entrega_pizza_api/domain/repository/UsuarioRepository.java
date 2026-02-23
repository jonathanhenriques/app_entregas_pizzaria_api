package com.diadema.app_entrega_pizza_api.domain.repository;


import com.diadema.app_entrega_pizza_api.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {


    UserDetails findByLogin(String login);

    // 1. SEGURANÇA: Validação de Cadastro
    // Útil para: O endpoint /register verificar se o login existe sem carregar todo o objeto UserDetails (senha, roles, etc).
    boolean existsByLogin(String login);
}