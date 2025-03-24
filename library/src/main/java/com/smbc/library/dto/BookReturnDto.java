package com.smbc.library.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookReturnDto {
   
   private Long bookRentId;
   private List<String> isbn;
}
