package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    // Método para buscar notícias por termo (título ou descrição)
    public Page<NoticiaDTO> buscarNoticiasPorTermo(String termo, Pageable pageable) {
        return noticiaRepository.findByTitleContainingOrDescriptionContaining(termo, termo, pageable);
    }

    // Método para buscar notícias por termo e categoria (sourceUrl)
    public Page<NoticiaDTO> buscarNoticiasPorTermoECategoria(String termo, String categoria, Pageable pageable) {
        return noticiaRepository.searchByTitleOrDescriptionAndCategory(termo, categoria, pageable);
    }
}
