package com.example.procurando.controller;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    // Endpoint para buscar notícias por termo (título ou descrição)
    @GetMapping("/buscar")
    public Page<NoticiaDTO> buscarNoticias(@RequestParam(required = false) String termo, Pageable pageable) {
        if (termo != null && !termo.isEmpty()) {
            return noticiaService.buscarNoticiasPorTermo(termo, pageable);
        } else {
            // Retorna uma página vazia ou uma resposta diferente, dependendo do seu caso
            return Page.empty();
        }
    }

    // Endpoint para buscar notícias por termo e categoria (sourceUrl)
    @GetMapping("/buscarPorCategoria")
    public Page<NoticiaDTO> buscarNoticiasPorCategoria(@RequestParam String termo, @RequestParam String categoria, Pageable pageable) {
        return noticiaService.buscarNoticiasPorTermoECategoria(termo, categoria, pageable);
    }
}
