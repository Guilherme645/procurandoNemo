package com.example.procurando.repository;

import com.example.procurando.model.NoticiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface NoticiaRepository extends ElasticsearchRepository<NoticiaDTO, String> {

    // Busca por título ou descrição com suporte a paginação
    Page<NoticiaDTO> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);

    // Busca por título ou descrição e categoria com suporte a paginação
    Page<NoticiaDTO> findByTitleContainingAndCategoriaOrDescriptionContainingAndCategoria(
            String title, String categoria1, String description, String categoria2, Pageable pageable);
}
