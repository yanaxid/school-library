package com.smbc.library.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smbc.library.dto.BookDto;
import com.smbc.library.dto.request.CustomPageRequest;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.service.BookService;
import com.smbc.library.util.ValidationUtil;
import com.smbc.library.validator.BookValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

   private final BookService bookService;
   private final BookValidator bookValidator;
   private final ValidationUtil validationUtil;

   @PostMapping("/add")
   public ResponseEntity<MessageResponse> addBook(@RequestBody BookDto request) {
      return validationUtil.processWithValidation(request, bookValidator, () -> bookService.addBook(request));
   }

   @GetMapping("/all")
   public ResponseEntity<MessageResponse> getAllBooks(@ModelAttribute CustomPageRequest customPageRequest) {
      return bookService.getAllBooks(customPageRequest.getPage("penulis,asc"));
   }

   @GetMapping("/{isbn}")
   public ResponseEntity<MessageResponse> getBook(@PathVariable String isbn) {
      return bookService.getBook(isbn);
   }

   @PutMapping("/update/{id}")
   public ResponseEntity<MessageResponse> updateBook(@PathVariable Long id, @RequestBody BookDto request) {
      return validationUtil.processWithValidation(request, bookValidator, () -> bookService.addBook(request));
   }

   @PutMapping("/delete/{id}")
   public ResponseEntity<MessageResponse> deleteBook(@PathVariable Long id) {
      return bookService.deleteBook(id);
   }

   
   
}