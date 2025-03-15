package com.smbc.library.repository;

import com.smbc.library.model.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRentRepository extends JpaRepository<BookRent, Long> {
   List<BookRent> findByMemberId(Long memberId);

   List<BookRent> findByBookId(Long bookId);
}