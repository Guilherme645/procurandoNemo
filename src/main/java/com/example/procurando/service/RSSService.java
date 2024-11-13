package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.model.RSSUrl;
import com.example.procurando.repository.NoticiaRepository;
import com.example.procurando.repository.RSSUrlRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RSSService {

    @Autowired
    private RSSUrlRepository rssUrlRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    public List<NoticiaDTO> processarFeedsESalvarNoElasticsearch(String termo) {
        List<NoticiaDTO> todasNoticias = new ArrayList<>();

        // Buscar todas as URLs no banco
        List<RSSUrl> rssUrls = rssUrlRepository.findAll();

        // Iterar sobre cada URL e processar os feeds
        for (RSSUrl rssUrl : rssUrls) {
            try {
                System.out.println("Processando feed: " + rssUrl.getUrl());

                // Baixar e parsear o feed RSS
                Document doc = Jsoup.connect(rssUrl.getUrl()).get();

                // Selecionar itens <item>
                Elements items = doc.select("item");

                // Iterar sobre os itens e extrair os dados
                for (Element item : items) {
                    String title = item.selectFirst("title").text();
                    String link = item.selectFirst("link").text();
                    String description = item.selectFirst("description").text();
                    String pubDate = item.selectFirst("pubDate").text();

                    // Filtrar por termo (caso fornecido)
                    if (termo == null || termo.isEmpty() || title.contains(termo) || description.contains(termo)) {
                        // Criar um objeto para armazenar os dados da notícia
                        NoticiaDTO noticia = new NoticiaDTO(title, link, description, pubDate);

                        // Salvar no Elasticsearch
                        noticiaRepository.save(noticia);

                        // Adicionar à lista de todas as notícias retornadas
                        todasNoticias.add(noticia);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar feed: " + rssUrl.getUrl());
                e.printStackTrace();
            }
        }

        return todasNoticias; // Retorna todas as notícias processadas e salvas
    }
}
