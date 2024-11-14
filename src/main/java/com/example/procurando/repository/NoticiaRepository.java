package com.example.procurando.repository;

import com.example.procurando.model.NoticiaDTO;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface NoticiaRepository extends ElasticsearchRepository<NoticiaDTO, String> {

    // Busca notícias cujo título ou descrição contenham o termo
    @Query("{\"bool\": {\"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"description\": \"?0\"}}]}}")
    Page<NoticiaDTO> searchByTitleOrDescription(String termo, Pageable pageable);
}

