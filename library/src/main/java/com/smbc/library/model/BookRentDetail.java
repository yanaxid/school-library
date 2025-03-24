package com.smbc.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.smbc.library.enums.RentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_rent_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BookRentDetail {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "book_rent_id", nullable = false) 
   @JsonBackReference
   private BookRent bookRent;

   @ManyToOne
   @JoinColumn(name = "book_id", nullable = false) 
   private Book book;

   @Enumerated(EnumType.STRING)
   @Column(name = "status", nullable = false)
   private RentStatus status;

   @Column(name = "return_date")
   private LocalDate returnDate;

   @Builder.Default
   @Column(name = "deleted", nullable = false)
   private boolean deleted = false;

   @CreatedDate
   @Column(name = "created_date", updatable = false)
   private LocalDateTime createdDate;

   @LastModifiedDate
   @Column(name = "modified_date")
   private LocalDateTime modifiedDate;
}

