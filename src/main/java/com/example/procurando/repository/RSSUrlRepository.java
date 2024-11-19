package com.example.procurando.repository;

import com.example.procurando.model.RSSUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RSSUrlRepository extends JpaRepository<RSSUrl, Long> {
    // Método para buscar um RSSUrl pelo campo URL
    RSSUrl findByUrl(String url);
}
