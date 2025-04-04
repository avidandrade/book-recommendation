package com.bookstore.book_store.Book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Override
    Optional<Book> findById(Long id);
    void deleteById(long id);
    
    
}
