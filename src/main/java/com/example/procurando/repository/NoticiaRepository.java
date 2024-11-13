package com.example.procurando.repository;

import com.example.procurando.model.NoticiaDTO;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticiaRepository extends ElasticsearchRepository<NoticiaDTO, String> {

    // Busca notícias cujo título ou descrição contenham o termo
    @Query("{\"bool\": {\"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"description\": \"?0\"}}]}}")
    List<NoticiaDTO> searchByTitleOrDescription(String termo);
}
