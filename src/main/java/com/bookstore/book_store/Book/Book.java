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
    private Long id;
 
    private String title;
    private String author;
    private String genre;
    private double rating;
    private double price;
    private String ISBN;
    private String Description;
    private int soldcopies;
    private String publisher;

    public Book(){}

    public Book(String title, String author, String genre, double rating, double price, String iSBN,
            String description, int soldcopies, String publisher) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
        this.price = price;
        this.ISBN = iSBN;
        Description = description;
        this.soldcopies = soldcopies;
        this.publisher = publisher;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        this.ISBN = iSBN;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getSoldcopies() {
        return soldcopies;
    }

    public void setSoldcopies(int soldcopies) {
        this.soldcopies = soldcopies;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
