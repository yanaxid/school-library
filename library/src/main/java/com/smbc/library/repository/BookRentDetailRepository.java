package com.smbc.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smbc.library.model.BookRentDetail;

@Repository
public interface BookRentDetailRepository extends JpaRepository<BookRentDetail, Long> {
   List<BookRentDetail> findByBookId(Long bookId);
}

