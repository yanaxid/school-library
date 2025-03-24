package com.smbc.library.controller;

import com.smbc.library.dto.BookRentDto;
import com.smbc.library.dto.BookReturnDto;
import com.smbc.library.dto.request.CustomPageRequest;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.enums.RentStatus;
import com.smbc.library.service.BookRentService;
import com.smbc.library.util.ValidationUtil;
import com.smbc.library.validator.BookRentValidator;
import com.smbc.library.validator.BookReturnValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book-rent")
@RequiredArgsConstructor
@Tag(name = "Book Rent API", description = "API untuk mengelola peminjaman dan pengembalian buku di perpustakaan")
public class BookRentController {

   private final BookRentService bookRentService;
   private final BookRentValidator bookRentValidator;
   private final BookReturnValidator bookReturnValidator;
   private final ValidationUtil validationUtil;

   @Operation(summary = "Peminjaman Buku", description = "Endpoint untuk melakukan peminjaman buku oleh member perpustakaan")
   @PostMapping("/rent")
   public ResponseEntity<MessageResponse> rentBook(@RequestBody BookRentDto request) {
      return validationUtil.processWithValidation(request, bookRentValidator, () -> bookRentService.rentBook(request));
   }

   @Operation(summary = "Pengembalian Buku", description = "Endpoint untuk mengembalikan buku yang telah dipinjam oleh member perpustakaan")
   @PutMapping("/return")
   public ResponseEntity<MessageResponse> returnBook(@RequestBody BookReturnDto request) {
      return validationUtil.processWithValidation(request, bookReturnValidator,
            () -> bookRentService.returnBook(request));
   }

   @Operation(summary = "Dapatkan Semua Data Peminjaman", description = "Mengambil daftar semua peminjaman buku")
   @GetMapping("/all")
   public ResponseEntity<MessageResponse> getAllRents(
         @RequestParam(required = false) String memberName,
         @RequestParam(required = false) String bookTitle,
         @RequestParam(required = false) RentStatus status,
         @ModelAttribute CustomPageRequest customPageRequest) {

      return bookRentService.getAllRent(memberName, bookTitle, status, customPageRequest.getPage("id,asc"));
   }

   @Operation(summary = "Dapatkan Detail Peminjaman", description = "Mendapatkan informasi peminjaman buku berdasarkan ID peminjaman")
   @GetMapping("/{id}")
   public ResponseEntity<MessageResponse> getRentBookById(@PathVariable @Min(1) Long id) {
      return bookRentService.getRentById(id);
   }

   @Operation(summary = "Hapus Data Peminjaman", description = "Menghapus data peminjaman buku berdasarkan ID peminjaman")
   @PutMapping("/delete/{id}")
   public ResponseEntity<MessageResponse> deleteRentBook(@PathVariable @Min(1) Long id) {
      return bookRentService.deleteBookRent(id);
   }
}
