package com.example.procurando.controller;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    @GetMapping("/buscar")
    public List<?> buscarNoticias(
            @RequestParam(required = false) String termo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean removerHtml) {
        return noticiaService.buscarNoticiasPorTermo(termo, page, size, removerHtml);
    }
}

