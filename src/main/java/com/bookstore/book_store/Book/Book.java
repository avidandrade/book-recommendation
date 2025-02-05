package com.bookstore.book_store.Book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="Book")

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    private String title;
    private String author;
    private String genre;
    private String description;
    private String isbn;
    private String coverImageUrl;

    public Book(String title, String author, String genre, String description, String isbn, String coverImageUrl) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.isbn = isbn;
        this.coverImageUrl = coverImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
    
}
