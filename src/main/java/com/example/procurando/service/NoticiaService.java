package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    public Page<NoticiaDTO> buscarNoticiasPorTermoECategoria(String termo, String categoria, Pageable pageable) {
        if (categoria != null && !categoria.isEmpty()) {
            // Busca por termo e categoria
            return noticiaRepository.findByTitleContainingAndCategoriaOrDescriptionContainingAndCategoria(
                    termo, categoria, termo, categoria, pageable);
        } else {
            // Busca somente por termo
            return noticiaRepository.findByTitleContainingOrDescriptionContaining(termo, termo, pageable);
        }
    }
}
