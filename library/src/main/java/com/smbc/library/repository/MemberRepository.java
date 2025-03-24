package com.smbc.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smbc.library.enums.RentStatus;
import com.smbc.library.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

   List<Member> findByDeletedFalse();

   Optional<Member> findByIdAndDeletedFalse(Long id);

   Optional<Member> findByEmailAndDeletedFalse(String email);

   Page<Member> findByDeletedFalse(Pageable pageable);

   @Query("""
             SELECT m FROM Member m
             WHERE EXISTS (
                 SELECT 1 FROM BookRent br
                 WHERE br.member.id = m.id
                 AND br.status = :status
             )
         """)
   Page<Member> findMembersWithUnreturnedBooks(@Param("status") RentStatus status, Pageable pageable);

}
