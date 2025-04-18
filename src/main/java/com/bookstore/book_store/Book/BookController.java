package com.bookstore.book_store.Book;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.book_store.Auth0.JwtUtil;

@RestController
public class BookController {
    @Autowired
    private final BookService bookService;
    private final JwtUtil jwtUtil;
    private String userId;

    public BookController(BookService bookService, JwtUtil jwtUtil){
        this.bookService = bookService;
        this.jwtUtil = jwtUtil;
    }

    @ModelAttribute
    public void extractUserId(@RequestHeader("Authorization") String token){
        this.userId = jwtUtil.extractUserId(token);
    }
    
    @GetMapping("/books")
    public List<Book> getAllBooks(){
        return bookService.getBooksByUserId(userId);
    }

    @PostMapping("/saveBooks")
    public List<Book> createBooks(@Validated @RequestBody List<Book> books){
        return bookService.createBooks(books);
    }
    @PostMapping("/saveBook")
    public Book createBook(@RequestBody Book book){
        return bookService.createBook(book, userId);
    }
    
    @GetMapping("/books/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn){
        return bookService.getBookByIsbnAndUserId(isbn,userId);
    }
    
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id){
         bookService.deleteBook(id,userId);
    }

    @GetMapping("/recommend")
    public List<Book> getRecommendedBooks(@RequestParam String input) {
        return bookService.fetchRecommendedBooks(input,userId);
    }

    @GetMapping("/moreBooks")
    public List<Book> getMoreBooks(@RequestParam String input, @RequestParam String titles) {
        List<String> titleList = Arrays.asList(titles.split(","));
        return bookService.fetchMoreBooks(input,titleList, userId);
    }


}