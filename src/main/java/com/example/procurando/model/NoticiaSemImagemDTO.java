package com.example.procurando.model;

public class NoticiaSemImagemDTO {
    private String id;
    private String title;
    private String description; // Já sem HTML
    private String link;
    private String pubDate;
    private String sourceUrl;

    // Construtores, Getters e Setters
    public NoticiaSemImagemDTO(String id, String title, String description, String link, String pubDate, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.sourceUrl = sourceUrl;
    }
}
