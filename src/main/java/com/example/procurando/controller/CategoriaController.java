package com.example.procurando.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @GetMapping
    public List<String> getCategorias() {
        // Retorna uma lista de categorias fixas
        return List.of("Tecnologia", "Educação", "Política", "Loterias");
    }
}
