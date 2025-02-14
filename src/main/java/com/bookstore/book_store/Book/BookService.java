package com.bookstore.book_store.Book;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.book_store.openAI.OllamaService;
import com.bookstore.book_store.s3.S3Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private S3Service s3Service;

    private final int maxResults = 1;

    private final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public List<Book> fetchRecommendedBooks(String userInput) {
        //Get a book title from Ollama AI
        String bookTitle = ollamaService.getRecommendedBookTitle(userInput);
        System.out.println(bookTitle);
        if (bookTitle == null || bookTitle.isEmpty()) {
            throw new RuntimeException("Ollama failed to generate a book title.");
        }

        // Fetch book details from Google Books API**
        return fetchBooks(bookTitle);
    }

    //Fetching books from API
    public List<Book> fetchBooks(String query) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + query + "&maxResults=" + maxResults;
        List<Book> bookDetailsList = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("items");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String title = item.path("volumeInfo").path("title").asText();
                    String authors = item.path("volumeInfo").path("authors").toString();
                    String description = item.path("volumeInfo").path("description").asText();
                    
                    // Extract ISBN
                    String isbn = "";
                    JsonNode industryIdentifiers = item.path("volumeInfo").path("industryIdentifiers");
                    if (industryIdentifiers.isArray()) {
                        for (JsonNode identifier : industryIdentifiers) {
                            if ("ISBN_13".equals(identifier.path("type").asText())) {
                                isbn = identifier.path("identifier").asText();
                                break;
                            }
                        }
                    }

                    // Extract genre (categories)
                    String genre = "";
                    JsonNode categories = item.path("volumeInfo").path("categories");
                    if (categories.isArray() && categories.size() > 0) {
                        genre = categories.get(0).asText();
                    }
                    String coverImageUrl = "";
                    JsonNode imageLinks = item.path("volumeInfo").path("imageLinks");
                    if (imageLinks.has("thumbnail")) {
                        coverImageUrl = imageLinks.path("thumbnail").asText();
                    }

                    bookDetailsList.add(new Book(title, authors, genre,description, isbn, coverImageUrl));
                }
            }
        } catch (IOException e) {
            return null;
        }
        return bookDetailsList;
    }
    
    public List<Book> getAllBooks(){ 
        return bookRepository.findAll();
    }

    public List<Book> createBooks(List<Book> books) {
        return bookRepository.saveAll(books);
    }

    public Book createBook(Book book){
        s3Service.uploadImageToS3(book.getCoverImageUrl(), book.getTitle());
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book updatedbook){
       Book existingBook = bookRepository.findById(id)
       .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        
        try{
            for(Field field : Book.class.getDeclaredFields()){
                field.setAccessible(true);
            
                Object newValue = field.get(updatedbook);

                if(newValue != null){
                    field.set(existingBook,newValue);
                }
            }
        }catch(IllegalAccessException e){
            return null;
        }

        return bookRepository.save(existingBook);
    }

}
