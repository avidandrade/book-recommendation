package com.bookstore.book_store.Book;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    public BookController(BookService bookService, JwtUtil jwtUtil){
        this.bookService = bookService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("/books")
    public List<Book> getAllBooks(@RequestHeader("Authorization")String token){
        String userId = jwtUtil.extractUserId(token);
        return bookService.getBooksByUserId(userId);
    }

    @PostMapping("/saveBooks")
    public List<Book> createBooks(@Validated @RequestBody List<Book> books){
        return bookService.createBooks(books);
    }
    @PostMapping("/saveBook")
    public Book createBook(@RequestBody Book book, @RequestHeader("Authorization") String token){
        String userId = jwtUtil.extractUserId(token);
        System.out.println("Extracted userId: " + userId); 
        return bookService.createBook(book, userId);
    }
    
    @GetMapping("/books/{id}")
    public Optional<Book> getBookByIdandUserId(@PathVariable Long id, @RequestHeader("Authorization") String token){
        String userId = jwtUtil.extractUserId(token);
        return bookService.getBookByIdandUserId(id,userId);
    }
    
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id, @RequestParam String userId){
         bookService.deleteBook(id,userId);
    }

    @GetMapping("/recommend")
    public List<Book> getRecommendedBooks(@RequestHeader("Authorization") String token, @RequestParam String input) {
        String userId = jwtUtil.extractUserId(token);
        return bookService.fetchRecommendedBooks(userId, input);
    }

    @GetMapping("/moreBooks")
    public List<Book> getMoreBooks(@RequestParam String input, @RequestParam String titles) {   
        List<String> titleList = Arrays.asList(titles.split(","));
        return bookService.fetchMoreBooks(input,titleList);
    }


}