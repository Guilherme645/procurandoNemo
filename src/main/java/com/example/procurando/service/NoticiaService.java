package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.model.NoticiaSemImagemDTO;
import com.example.procurando.repository.NoticiaRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    /**
     * Busca notícias filtradas por termo com suporte a paginação.
     * Se `removerHtml` for true, retorna o DTO sem HTML na descrição.
     *
     * @param termo        Termo de busca.
     * @param page         Página atual.
     * @param size         Tamanho da página.
     * @param removerHtml  Indica se o HTML deve ser removido das descrições.
     * @return Lista de notícias com ou sem HTML nas descrições.
     */
    public List<?> buscarNoticiasPorTermo(String termo, int page, int size, boolean removerHtml) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NoticiaDTO> noticiasPage;

        // Busca no repositório (com ou sem termo)
        if (termo == null || termo.isEmpty()) {
            noticiasPage = noticiaRepository.findAll(pageable);
        } else {
            noticiasPage = noticiaRepository.searchByTitleOrDescription(termo, pageable);
        }

        // Retorna conforme a necessidade de remover HTML
        if (removerHtml) {
            return noticiasPage.getContent().stream()
                    .map(noticia -> new NoticiaSemImagemDTO(
                            noticia.getId(),
                            noticia.getTitle(),
                            limparHtml(noticia.getDescription()), // Remove HTML aqui
                            noticia.getLink(),
                            noticia.getPubDate(),
                            noticia.getSourceUrl()
                    ))
                    .collect(Collectors.toList());
        }

        return noticiasPage.getContent(); // Retorna as notícias originais
    }

    /**
     * Remove todas as tags HTML do texto usando Jsoup.
     *
     * @param texto Texto com conteúdo HTML.
     * @return Texto limpo sem HTML.
     */
    private String limparHtml(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return Jsoup.parse(texto).text(); // Remove o HTML
    }
}
