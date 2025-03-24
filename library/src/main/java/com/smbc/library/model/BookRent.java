package com.smbc.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smbc.library.enums.RentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "book_rent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BookRent {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "member_id", nullable = false)
   private Member member;

   @Builder.Default
   @OneToMany(mappedBy = "bookRent", cascade = CascadeType.ALL)
   @JsonManagedReference
   private List<BookRentDetail> bookRentDetails = new ArrayList<>();

   @Column(name = "rent_date", nullable = false)
   private LocalDate rentDate;

   @Column(name = "due_date", nullable = false)
   private LocalDate dueDate;

   @Column(name = "return_date")
   private LocalDate returnDate;

   @Enumerated(EnumType.STRING) 
   @Column(name = "status", nullable = false)
   private RentStatus status;

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