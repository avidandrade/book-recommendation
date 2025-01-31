package com.bookstore.book_store.Book;

public class BookDetails {
    private String title;
    private String author;
    private String genre;
    private String description;
    
    private String isbn;

    public BookDetails(String title, String author, String genre, String description, String isbn) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.isbn = isbn;
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


    
}
