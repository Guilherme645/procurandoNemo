package com.example.procurando.controller;

import com.example.procurando.service.RSSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RSSController {

    private static final Logger logger = LoggerFactory.getLogger(RSSController.class);

    @Autowired
    private RSSService rssService;

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
}
