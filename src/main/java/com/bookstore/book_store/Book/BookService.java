package com.bookstore.book_store.Book;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookstore.book_store.Ollama3.OllamaService;
import com.bookstore.book_store.s3.S3Service;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final OllamaService ollamaService;
    private final S3Service s3Service;
    private final GoogleService googleService;

    public BookService(BookRepository bookRepository, OllamaService ollamaService,S3Service s3Service,GoogleService googleService){
        this.bookRepository = bookRepository;
        this.ollamaService = ollamaService;
        this.s3Service = s3Service;
        this.googleService = googleService;
    }

    public List<Book> fetchRecommendedBooks(String userInput) {
        //Get a book title from Ollama AI
        List<String> bookTitles = ollamaService.getRecommendedBookTitle(userInput);
        return googleService.fetchRecommendedBooks(bookTitles);
    }

    public List<Book> fetchMoreBooks(String userInput, List<String> titles) {
        //Titles are sent so that the model avoids sending the same book.
        List<String> bookTitles = ollamaService.getMoreBooks(userInput, titles);
        if (bookTitles == null || bookTitles.isEmpty()) {
            throw new RuntimeException("Ollama failed to generate more recommended book titles!");
        }

        // Fetch book details from Google Books API**
        return googleService.fetchRecommendedBooks(bookTitles);
    }                                                                                         
    public List<Book> getAllBooks(){ 
        return bookRepository.findAll();
    }

    public List<Book> createBooks(List<Book> books) {
        return bookRepository.saveAll(books);
    }

    public Book createBook(Book book){
        try{
            s3Service.uploadImageToS3(book.getCoverImageUrl(), book.getTitle());
        }catch(Exception e){
            System.out.println("Error uploading image to S3");
        }
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
