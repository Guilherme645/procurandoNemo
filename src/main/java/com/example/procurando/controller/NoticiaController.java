package com.example.procurando.controller;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    @GetMapping("/buscar")
    public Page<NoticiaDTO> buscarNoticias(
            @RequestParam String termo,
            @RequestParam(required = false) String categoria,
            Pageable pageable) {
        return noticiaService.buscarNoticiasPorTermoECategoria(termo, categoria, pageable);
    }

}

