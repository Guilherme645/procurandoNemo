package com.example.procurando.controller;

import com.example.procurando.model.RSSUrl;
import com.example.procurando.repository.RSSUrlRepository;
import com.example.procurando.service.RSSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/noticias")
public class RSSController {

    private static final Logger logger = LoggerFactory.getLogger(RSSController.class);

    @Autowired
    private RSSService rssService;

    @Autowired
    private RSSUrlRepository rssUrlRepository;

    @GetMapping("/processarFeeds")
    public String processarFeeds() {
        try {
            if (rssService == null) {
                logger.error("RSSService não foi injetado corretamente!");
                return "Erro: Serviço RSS não está disponível.";
            }

            rssService.processarTodosFeedsESalvarNoElasticsearch();
            logger.info("Todas as URLs e categorias foram processadas com sucesso.");
            return "Sucesso: Feeds e categorias foram processados corretamente!";
        } catch (Exception e) {
            logger.error("Erro ao processar feeds: ", e);
            return "Erro: Ocorreu um problema ao processar os feeds. Consulte os logs para mais detalhes.";
        }
    }

    @GetMapping("/categorias")
    public List<String> getCategorias() {
        try {
            return rssUrlRepository.findAll().stream()
                    .map(RSSUrl::getCategoria)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar categorias: ", e);
            throw new RuntimeException("Erro ao buscar categorias: " + e.getMessage());
        }
    }

    @PostMapping("/adicionar-feed")
    public ResponseEntity<String> adicionarFeed(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        String categorias = body.get("categorias");

        if (url == null || categorias == null) {
            return ResponseEntity.badRequest().body("Os campos 'url' e 'categorias' são obrigatórios.");
        }

        try {
            // Adicionar o feed RSS ao banco de dados
            rssService.adicionarFeedRss(url, categorias);

            // Processar o feed específico
            rssService.processarFeedEspecifico(url);

            // Forçar refresh no índice do Elasticsearch
            rssService.refreshIndiceElasticsearch();

            return ResponseEntity.ok("Feed adicionado e processado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao adicionar feed: ", e);
            return ResponseEntity.status(500).body("Erro ao adicionar feed: " + e.getMessage());
        }
    }
}