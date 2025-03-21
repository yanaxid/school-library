package com.smbc.library.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookRentDto {
    private Long bookId;
    private Long memberId;
    private LocalDate dueDate;
}