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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RSSService {

    @Autowired
    private RSSUrlRepository rssUrlRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    /**
     * Método principal para processar os feeds RSS, salvar no Elasticsearch e realizar busca.
     *
     * @param termo O termo para filtro (opcional).
     * @param categoria A categoria para filtro (opcional).
     * @param page Número da página para a paginação.
     * @param size Tamanho da página para a paginação.
     * @return Lista de notícias processadas.
     */
    public List<NoticiaDTO> processarFeedsESalvarNoElasticsearch(String termo, String categoria, int page, int size) {
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
                    String title = limparHtml(item.selectFirst("title").text());
                    String link = limparHtml(item.selectFirst("link").text());
                    String description = limparHtml(item.selectFirst("description").text());
                    String pubDate = limparHtml(item.selectFirst("pubDate").text());

                    // Filtrar por termo (caso fornecido)
                    if (termo == null || termo.isEmpty() || title.contains(termo) || description.contains(termo)) {
                        // Criar um objeto para armazenar os dados da notícia
                        String sourceUrl = rssUrl.getUrl(); // Define o sourceUrl como a URL do feed RSS

                        NoticiaDTO noticia = new NoticiaDTO(title, link, description, pubDate, sourceUrl);

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

        // Realizar a busca no Elasticsearch
        List<NoticiaDTO> noticiasFiltradas = buscarNoticiasPorTermoECategoria(termo, categoria, page, size);

        return noticiasFiltradas; // Retorna as notícias filtradas e processadas
    }

    /**
     * Método para realizar a busca no Elasticsearch com filtro por termo e categoria.
     *
     * @param termo O termo para busca (título ou descrição).
     * @param categoria A categoria para filtro (sourceUrl).
     * @param page Número da página para a paginação.
     * @param size Tamanho da página para a paginação.
     * @return Lista de notícias encontradas.
     */
    private List<NoticiaDTO> buscarNoticiasPorTermoECategoria(String termo, String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Realizar a busca no Elasticsearch
        Page<NoticiaDTO> resultadoBusca = noticiaRepository.searchByTitleOrDescriptionAndCategory(termo, categoria, pageable);

        // Retornar as notícias encontradas
        return resultadoBusca.getContent();
    }

    /**
     * Método para limpar código HTML e retornar apenas texto puro.
     *
     * @param texto O texto contendo HTML.
     * @return Texto limpo (sem tags HTML).
     */
    private String limparHtml(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto; // Retorna o texto vazio ou nulo se for o caso
        }
        return Jsoup.parse(texto).text(); // Remove todas as tags HTML e retorna o texto puro
    }
}
