package com.smbc.library.repository;

import com.smbc.library.enums.RentStatus;
import com.smbc.library.model.BookRent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRentRepository extends JpaRepository<BookRent, Long>, JpaSpecificationExecutor<BookRent> {
   List<BookRent> findByMemberId(Long memberId);
   Optional<BookRent> findByIdAndDeletedFalse(Long id);

   boolean existsByMemberIdAndStatus(Long memberId, RentStatus status);

}