package com.diadema.app_entrega_pizza_api.domain.service;

import com.diadema.app_entrega_pizza_api.domain.exception.RegraNegocioException;
import com.diadema.app_entrega_pizza_api.domain.model.Motoboy;
import com.diadema.app_entrega_pizza_api.domain.repository.MotoboyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MotoboyService {

    @Autowired
    private MotoboyRepository motoboyRepository;

    public Page<Motoboy> listarTodos(Pageable pageable) {
        return this.motoboyRepository.findAll(pageable);
    }

    public Motoboy buscarOuFalhar(UUID id) {
        return this.motoboyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motoboy não encontrado com ID: " + id));
    }

    @Transactional
    public Motoboy salvar(Motoboy motoboy) {
        // Verifica se já existe alguém com esse CPF
        var motoboyExistente = this.motoboyRepository.findByCpf(motoboy.getCpf());

        // Se encontrou alguém com esse CPF...
        if (motoboyExistente.isPresent()) {
            // ...e esse alguém NÃO é o motoboy que estou tentando salvar agora (caso de atualização)
            // Entra no IF se for um cadastro NOVO ou se for OUTRO motoboy usando o CPF
            if (!motoboyExistente.get().getId().equals(motoboy.getId())) {
                throw new RegraNegocioException("Já existe um motoboy cadastrado com este CPF: " + motoboy.getCpf());
            }
        }

        return this.motoboyRepository.save(motoboy);
    }

    @Transactional
    public void excluir(UUID id) {
        if (!this.motoboyRepository.existsById(id)) {
            throw new EntityNotFoundException("Motoboy não encontrado com ID: " + id);
        }
        motoboyRepository.deleteById(id);
    }
}