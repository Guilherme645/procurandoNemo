package com.example.procurando.repository;

import com.example.procurando.model.NoticiaDTO;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface NoticiaRepository extends ElasticsearchRepository<NoticiaDTO, String> {

    // Busca notícias cujo título ou descrição contenham o termo
    Page<NoticiaDTO> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);

    // Busca notícias cujo título ou descrição contenham o termo e filtra pela categoria (sourceUrl)
    @Query("{\"bool\": {\"must\": [{\"bool\": {\"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"description\": \"?0\"}}]}}, {\"term\": {\"sourceUrl\": \"?1\"}}]}}")
    Page<NoticiaDTO> searchByTitleOrDescriptionAndCategory(String termo, String categoria, Pageable pageable);
}
