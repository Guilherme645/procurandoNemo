package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    // Busca diretamente no Elasticsearch
    public List<NoticiaDTO> buscarNoticiasPorTermo(String termo) {
        if (termo == null || termo.isEmpty()) {
            // Retorna todas as notícias caso nenhum termo seja fornecido
            return (List<NoticiaDTO>) noticiaRepository.findAll();
        }

        // Busca notícias pelo termo no Elasticsearch
        return noticiaRepository.searchByTitleOrDescription(termo);
    }
}
