package com.bookstore.book_store.Book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Override
    Optional<Book> findById(Long id);
    void deleteById(long id);
    List<Book> findByUserId(String userId);
    Optional<Book> findByIdAndUserId(Long id, String userId);
    Optional<Book> findByIsbnAndUserId(String isbn, String userId);
}
