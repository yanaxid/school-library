package com.smbc.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book_rent")
@EntityListeners(AuditingEntityListener.class)
public class BookRent {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "book_id", nullable = false)
   private Book book;

   @ManyToOne
   @JoinColumn(name = "member_id", nullable = false)
   private Member member;

   @Column(name = "rent_date", nullable = false)
   private LocalDate rentDate;

   @Column(name = "due_date", nullable = false)
   private LocalDate dueDate;

   @Column(name = "return_date")
   private LocalDate returnDate;

   @Column(name = "status", nullable = false)
   private String status;

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
