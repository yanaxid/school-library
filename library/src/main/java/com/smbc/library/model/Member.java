package com.smbc.library.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private Long id;

   @Column(name = "nama", nullable = false)
   private String nama;

   @Column(name = "alamat", nullable = false)
   private String alamat;

   @Column(name = "email", nullable = false, unique = true)
   private String email;

   @Builder.Default
   @Column(name = "deleted", nullable = false)
   private boolean deleted = false;

   @Column(name = "created_date", nullable = false, updatable = false)
   @CreatedDate
   private LocalDateTime createdDate;

   @Column(name = "modified_date", nullable = false)
   @LastModifiedDate
   private LocalDateTime modifiedDate;

   
}
