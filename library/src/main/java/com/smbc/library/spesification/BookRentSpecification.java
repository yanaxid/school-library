package com.smbc.library.spesification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.smbc.library.enums.RentStatus;
import com.smbc.library.model.BookRent;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BookRentSpecification {

   private BookRentSpecification(){}

   public static Specification<BookRent> filterBy(String memberName, String bookTitle, RentStatus status) {
       return (Root<BookRent> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
           List<Predicate> predicates = new ArrayList<>();

           if (memberName != null && !memberName.isBlank()) {
               predicates.add(cb.like(cb.lower(root.get("member").get("nama")), "%" + memberName.toLowerCase() + "%"));
           }

           if (bookTitle != null && !bookTitle.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("bookRentDetails").get("book").get("judul")), "%" + bookTitle.toLowerCase() + "%"));
        }

           if (status != null) {
               predicates.add(cb.equal(root.get("status"), status));
           }

           return cb.and(predicates.toArray(new Predicate[0]));
       };
   }
}
