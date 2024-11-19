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
import co.elastic.clients.elasticsearch.ElasticsearchClient;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
public class RSSService {

    private static final Logger logger = LoggerFactory.getLogger(RSSService.class);

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private RSSUrlRepository rssUrlRepository;

    public void processarTodosFeedsESalvarNoElasticsearch() {
        List<RSSUrl> rssUrls = rssUrlRepository.findAll();

        if (rssUrls.isEmpty()) {
            logger.warn("Nenhuma URL encontrada na tabela rss_urls.");
            return;
        }

        for (RSSUrl rssUrl : rssUrls) {
            try {
                logger.info("Processando feed: {}", rssUrl.getUrl());

                Document doc = Jsoup.connect(rssUrl.getUrl())
                        .timeout(10000)
                        .get();

                Elements items = doc.select("item");

                if (items.isEmpty()) {
                    logger.warn("Nenhum item encontrado no feed: {}", rssUrl.getUrl());
                    continue;
                }

                for (Element item : items) {
                    String title = safeSelectText(item, "title");
                    String link = safeSelectText(item, "link");
                    String description = safeSelectText(item, "description");
                    String pubDate = safeSelectText(item, "pubDate");
                    String categoria = rssUrl.getCategoria();
                    String faviconUrl = gerarFaviconUrl(link);
                    String sourceUrl = extrairSourceUrl(link);

                    // Criar objeto NoticiaDTO
                    NoticiaDTO noticia = new NoticiaDTO(
                            title,
                            link,
                            limparHtml(description),
                            pubDate,
                            categoria,
                            faviconUrl,
                            sourceUrl
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

    private String limparHtml(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return Jsoup.parse(texto).text();
    }

    private String gerarFaviconUrl(String link) {
        try {
            URI uri = new URI(link);
            return uri.getScheme() + "://" + uri.getHost() + "/favicon.ico";
        } catch (Exception e) {
            return null;
        }
    }

    private String extrairSourceUrl(String link) {
        try {
            URI uri = new URI(link);
            return uri.getScheme() + "://" + uri.getHost();
        } catch (Exception e) {
            return null;
        }
    }

    private String safeSelectText(Element element, String tag) {
        Element selectedElement = element.selectFirst(tag);
        return selectedElement != null ? selectedElement.text() : "";
    }

    public void adicionarFeedRss(String url, String categorias) {
        RSSUrl rssUrl = new RSSUrl();
        rssUrl.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        rssUrl.setUrl(url);
        rssUrl.setCategoria(categorias);
        rssUrlRepository.save(rssUrl);
    }

    public void refreshIndiceElasticsearch() {
        try {
            elasticsearchClient.indices().refresh(r -> r.index("noticias"));
            logger.info("Índice 'noticias' atualizado com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao atualizar o índice 'noticias': ", e);
        }
    }

    public void processarFeedEspecifico(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .get();

            Elements items = doc.select("item");

            if (items.isEmpty()) {
                logger.warn("Nenhum item encontrado no feed: {}", url);
                return;
            }

            // Buscar a categoria associada ao feed
            RSSUrl rssUrl = rssUrlRepository.findByUrl(url);
            if (rssUrl == null) {
                logger.error("Feed não encontrado no banco de dados para a URL: {}", url);
                return;
            }

            for (Element item : items) {
                String title = safeSelectText(item, "title");
                String link = safeSelectText(item, "link");
                String description = safeSelectText(item, "description");
                String pubDate = safeSelectText(item, "pubDate");
                String faviconUrl = gerarFaviconUrl(link);
                String sourceUrl = extrairSourceUrl(link);

                // Criar objeto NoticiaDTO
                NoticiaDTO noticia = new NoticiaDTO(
                        title,
                        link,
                        limparHtml(description),
                        pubDate,
                        rssUrl.getCategoria(), // Usar a categoria do banco
                        faviconUrl,
                        sourceUrl
                );

                // Salvar a notícia no Elasticsearch
                noticiaRepository.save(noticia);
                logger.info("Notícia '{}' salva com sucesso no Elasticsearch.", title);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar feed: {}", url, e);
        }
    }

}
