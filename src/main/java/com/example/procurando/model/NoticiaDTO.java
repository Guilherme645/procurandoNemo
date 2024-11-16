package com.example.procurando.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "noticias")
public class NoticiaDTO {

    @Id
    private String id;

    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String categoria;

    // Construtor padrão
    public NoticiaDTO() {
    }

    // Construtor com parâmetros
    public NoticiaDTO(String title, String link, String description, String pubDate, String categoria) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.categoria = categoria;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
