package com.bookstore.book_store.Book;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    private String getUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    @GetMapping("/books")
    public List<Book> getAllBooks(){
        String userId = getUserId();
        return bookService.getBooksByUserId(userId);
    }

    @PostMapping("/saveBooks")
    public List<Book> createBooks(@Validated @RequestBody List<Book> books){
        return bookService.createBooks(books);
    }
    @PostMapping("/saveBook")
    public Book createBook(@RequestBody Book book){
        String userId = getUserId();
        return bookService.createBook(book, userId);
    }
    
    @GetMapping("/books/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn){
        String userId = getUserId();
        return bookService.getBookByIsbnAndUserId(isbn,userId);
    }
    
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id){
        String userId = getUserId();
         bookService.deleteBook(id,userId);
    }

    @GetMapping("/recommend")
    public List<Book> getRecommendedBooks(@RequestParam String input) {
        String userId = getUserId();
        return bookService.fetchRecommendedBooks(input,userId);
    }

    @GetMapping("/moreBooks")
    public List<Book> getMoreBooks(@RequestParam String input, @RequestParam String titles) {
        List<String> titleList = Arrays.asList(titles.split(","));
        String userId = getUserId();
        return bookService.fetchMoreBooks(input,titleList, userId);
    }


}