package com.example.procurando.service;

import com.example.procurando.model.NoticiaDTO;
import com.example.procurando.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    /**
     * Busca notícias por termo, categoria, faviconUrl e sourceUrl, incluindo paginação.
     *
     * @param termo     Termo de busca no título, descrição, faviconUrl ou sourceUrl.
     * @param categoria Categoria para filtrar os resultados.
     * @param pageable  Parâmetros de paginação.
     * @return Página contendo as notícias encontradas.
     */
    public Page<NoticiaDTO> buscarNoticiasPorTermoECategoria(String termo, String categoria, Pageable pageable) {
        Page<NoticiaDTO> noticias;

        if (categoria != null && !categoria.isEmpty()) {
            // Busca por termo, categoria e outros campos
            noticias = noticiaRepository.findByTitleContainingAndCategoriaOrDescriptionContainingAndCategoriaOrFaviconUrlContainingAndCategoriaOrSourceUrlContainingAndCategoria(
                    termo, categoria, termo, categoria, termo, categoria, termo, categoria, pageable);
        } else {
            // Busca somente por termo nos campos relevantes
            noticias = noticiaRepository.findByTitleContainingOrDescriptionContainingOrFaviconUrlContainingOrSourceUrlContaining(
                    termo, termo, termo, termo, pageable);
        }

        // Retornar todas as notícias encontradas com os campos já disponíveis no documento
        return noticias;
    }
}
