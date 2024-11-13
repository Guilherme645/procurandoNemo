package com.example.procurando.repository;

import com.example.procurando.model.RSSUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RSSUrlRepository extends JpaRepository<RSSUrl, Long> {
}
