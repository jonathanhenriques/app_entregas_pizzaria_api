package com.diadema.app_entrega_pizza_api.domain.controller;


import com.diadema.app_entrega_pizza_api.api.security.TokenService;
import com.diadema.app_entrega_pizza_api.domain.model.Usuario;
import com.diadema.app_entrega_pizza_api.domain.model.dto.AuthenticationDTO;
import com.diadema.app_entrega_pizza_api.domain.model.dto.RegisterDTO;
import com.diadema.app_entrega_pizza_api.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 1. Recuperamos o objeto Usuario completo que foi autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        var token = tokenService.gerarToken(usuario);

        // 2. Retornamos um Map com Token, ID e Role
        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", usuario.getId(),        // O UUID do usuário
                "role", usuario.getRole()     // Se é ADMIN ou USER
        ));
    }

    @PostMapping("/register")
    // MUDANÇA: De ResponseEntity para ResponseEntity<Void> (sem corpo)
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().build(); // Simplificado
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        Usuario newUser = new Usuario();
        newUser.setLogin(data.login());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}