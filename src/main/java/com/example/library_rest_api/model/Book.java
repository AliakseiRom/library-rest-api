package com.example.library_rest_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "genre")
    private String genre;

    @Column(name = "available")
    private Boolean available;

    public Book(Long id, String isbn, String title, String author, String description, String genre, Boolean available) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.available = available;
    }

    public Book() {}
}
