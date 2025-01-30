package com.bookstore.book_store.Book;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    private final int maxResults = 5;

    private final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    //Fetching books from API
    public String fetchBooks(String query){
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + query + "&maxResults=" + maxResults;
        try{
            //The query is created here that creates a url from our application, parameter of userinput and maxResults.

            //RestTemplate object is creating a get request to the google books api by sending the query url and storing the payload in response.
            String response = restTemplate.getForObject(url, String.class);
            return response;
        }catch(Exception e){
            e.printStackTrace();
            return "Error fetching books";
        }
    }
    
    public List<Book> getAllBooks(){ 
        return bookRepository.findAll();
    }

    public List<Book> createBooks(List<Book> books) {
        return bookRepository.saveAll(books);
    }

    public Book createBook(Book book){
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
             e.printStackTrace();
        }

        return bookRepository.save(existingBook);
    }

}
