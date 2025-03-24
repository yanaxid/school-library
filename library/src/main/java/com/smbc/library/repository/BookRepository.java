package com.smbc.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smbc.library.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
   Optional<Book> findByIsbnAndDeletedFalse(String isbn);
   Page<Book> findByDeletedFalse(Pageable pageable);
   List<Book> findByIsbnInAndDeletedFalse(List<String> isbns);
}
