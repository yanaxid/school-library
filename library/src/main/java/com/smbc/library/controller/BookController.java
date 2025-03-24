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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Tag(name = "Book API", description = "API untuk mengelola buku di perpustakaan, termasuk penambahan, pencarian, pembaruan, dan penghapusan buku.")
public class BookController {

   private final BookService bookService;
   private final BookValidator bookValidator;
   private final ValidationUtil validationUtil;

   @Operation(summary = "Tambah buku", description = "Menambahkan buku baru ke dalam sistem perpustakaan")
   @PostMapping("/add")
   public ResponseEntity<MessageResponse> addBook(@RequestBody BookDto request) {
      return validationUtil.processWithValidation(request, bookValidator, () -> bookService.addBook(request));
   }

   @Operation(summary = "Gett all books", description = "Mengambil daftar semua buku yang tersedia di perpustakaan dengan opsi pagination dan sorting")
   @GetMapping("/all")
   public ResponseEntity<MessageResponse> getAllBooks(@ModelAttribute CustomPageRequest customPageRequest) {
      return bookService.getAllBooks(customPageRequest.getPage("penulis,asc"));
   }

   @Operation(summary = "Get book by ISBN", description = "Mencari informasi buku berdasarkan ISBN.")
   @GetMapping("/{isbn}")
   public ResponseEntity<MessageResponse> getBook(@PathVariable @Min(0) String isbn) {
      return bookService.getBook(isbn);
   }

   @Operation(summary = "Update Book", description = "Memperbarui informasi buku yang ada berdasarkan ID buku")
   @PutMapping("/update/{id}")
   public ResponseEntity<MessageResponse> updateBook(@PathVariable @Min(0) Long id, @RequestBody BookDto request) {
      return validationUtil.processWithValidation(request, bookValidator, () -> bookService.addBook(request));
   }

   @Operation(summary = "Delete Book", description = "Menghapus buku dari sistem berdasarkan ID buku")
   @PutMapping("/delete/{id}")
   public ResponseEntity<MessageResponse> deleteBook(@PathVariable @Min(0) Long id) {
      return bookService.deleteBook(id);
   }

}