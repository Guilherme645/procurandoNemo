package com.example.procurando.repository;

import com.example.procurando.model.NoticiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface NoticiaRepository extends ElasticsearchRepository<NoticiaDTO, String> {

    // Busca por título, descrição, faviconUrl ou sourceUrl com suporte a paginação
    Page<NoticiaDTO> findByTitleContainingOrDescriptionContainingOrFaviconUrlContainingOrSourceUrlContaining(
            String title, String description, String faviconUrl, String sourceUrl, Pageable pageable);

    // Busca por título, descrição, categoria, faviconUrl ou sourceUrl com suporte a paginação
    Page<NoticiaDTO> findByTitleContainingAndCategoriaOrDescriptionContainingAndCategoriaOrFaviconUrlContainingAndCategoriaOrSourceUrlContainingAndCategoria(
            String title, String categoria1, String description, String categoria2, String faviconUrl, String categoria3, String sourceUrl, String categoria4, Pageable pageable);
}

