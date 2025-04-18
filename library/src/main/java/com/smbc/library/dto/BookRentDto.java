package com.smbc.library.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class BookRentDto {
   private List<String> isbn;
   private Long memberId;
   private LocalDate dueDate;
}

