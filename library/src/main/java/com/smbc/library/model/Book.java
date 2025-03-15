package com.smbc.library.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class Book {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private Long id;

   @Column(name = "judul", nullable = false)
   private String judul; 

   @Column(name = "penulis", nullable = false)
   private String penulis;  

   @Column(name = "penerbit", nullable = false)
   private String penerbit;  

   @Column(name = "tahun_terbit", nullable = false)
   private Integer tahunTerbit;  

   @Column(name = "isbn", unique = true, nullable = false)
   private String isbn;  

   @Column(name = "kategori", nullable = false)
   private String kategori;  

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

