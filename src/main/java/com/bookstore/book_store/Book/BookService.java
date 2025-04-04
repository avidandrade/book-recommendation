package com.bookstore.book_store.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.book_store.Ollama3.OllamaService;
import com.bookstore.book_store.s3.S3Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private S3Service s3Service;

    private final int maxResults = 5;

    private final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Book> fetchRecommendedBooks(String userInput) {
        //Get a book title from Ollama AI
        List<String> bookTitles = ollamaService.getRecommendedBookTitle(userInput);
        System.out.println(bookTitles);
        if (bookTitles == null || bookTitles.isEmpty()) {
            throw new RuntimeException("Ollama failed to generate a book title.");
        }

        // Fetch book details from Google Books API**
        return fetchBooks(bookTitles);
    }

    public List<Book> fetchMoreBooks(String userInput, List<String> titles) {
        //Titles are sent so that the model avoids sending the same book.
        List<String> bookTitles = ollamaService.getMoreBooks(userInput, titles);
        if (bookTitles == null || bookTitles.isEmpty()) {
            throw new RuntimeException("Ollama failed to generate a book title.");
        }

        // Fetch book details from Google Books API**
        return fetchBooks(bookTitles);
    }

    //Fetching books from API
    public List<Book> fetchBooks(List<String> bookTitles) {
            List<Book> books = new ArrayList<>();

            for(String title : bookTitles){
                String url = API_URL + title + "&maxResults=" + maxResults;

                String response = restTemplate.getForObject(url, String.class);
                books.add(parseResponse(response));
            }
            return books;
    }
    
    public Book parseResponse(String response){
        try{
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("items");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String title = item.path("volumeInfo").path("title").asText();
                    String authors = item.path("volumeInfo").path("authors").toString();
                    String description = item.path("volumeInfo").path("description").asText();
                    int rating = item.path("volumeInfo").path("averageRating").asInt();
                    
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

                    return new Book(title, authors, genre,description, isbn, coverImageUrl, rating);
                }       
            }
        }catch(IOException e){
            return null;
        }
        return null;
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
