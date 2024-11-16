package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.model.RSSUrl;
import com.example.procurando.repository.NoticiaRepository;
import com.example.procurando.repository.RSSUrlRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RSSService {

    private static final Logger logger = LoggerFactory.getLogger(RSSService.class);

    @Autowired
    private RSSUrlRepository rssUrlRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    public void processarTodosFeedsESalvarNoElasticsearch() {
        // Obter todas as URLs e categorias da tabela rss_urls
        List<RSSUrl> rssUrls = rssUrlRepository.findAll();

        if (rssUrls.isEmpty()) {
            logger.warn("Nenhuma URL encontrada na tabela rss_urls.");
            return;
        }

        for (RSSUrl rssUrl : rssUrls) {
            try {
                logger.info("Processando feed: {}", rssUrl.getUrl());

                // Fazer o download e parse do RSS
                Document doc = Jsoup.connect(rssUrl.getUrl())
                        .timeout(10000) // Timeout de 10 segundos
                        .get();

                Elements items = doc.select("item");

                if (items.isEmpty()) {
                    logger.warn("Nenhum item encontrado no feed: {}", rssUrl.getUrl());
                    continue;
                }

                // Iterar sobre os itens do feed
                for (Element item : items) {
                    String title = safeSelectText(item, "title");
                    String link = safeSelectText(item, "link");
                    String description = safeSelectText(item, "description");
                    String pubDate = safeSelectText(item, "pubDate");

                    // Criar o objeto de notícia
                    NoticiaDTO noticia = new NoticiaDTO(
                            title,
                            link,
                            limparHtml(description),
                            pubDate,
                            rssUrl.getCategoria() // Associar a categoria do feed
                    );

                    // Salvar no Elasticsearch
                    noticiaRepository.save(noticia);
                    logger.info("Notícia '{}' salva com sucesso no Elasticsearch.", title);
                }

            } catch (Exception e) {
                logger.error("Erro ao processar feed: {}", rssUrl.getUrl(), e);
            }
        }
    }

    // Método para limpar HTML das descrições
    private String limparHtml(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return Jsoup.parse(texto).text();
    }

    // Método auxiliar para selecionar e validar elementos
    private String safeSelectText(Element element, String tag) {
        Element selectedElement = element.selectFirst(tag);
        return selectedElement != null ? selectedElement.text() : "";
    }
}
