package com.bookstore.book_store;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    
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
