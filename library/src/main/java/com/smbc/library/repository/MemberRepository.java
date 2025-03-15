package com.smbc.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smbc.library.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

   List<Member> findByDeletedFalse();

   Optional<Member> findByIdAndDeletedFalse(Long id);

   Page<Member> findByDeletedFalse(Pageable pageable);

}
