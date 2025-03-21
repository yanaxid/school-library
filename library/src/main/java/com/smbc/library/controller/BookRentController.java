package com.smbc.library.controller;

import com.smbc.library.dto.BookRentDto;
import com.smbc.library.dto.request.CustomPageRequest;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.service.BookRentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book-rent")
@RequiredArgsConstructor
public class BookRentController {

   private final BookRentService bookRentService;
   

   @PostMapping("/rent")
   public ResponseEntity<MessageResponse> rentBook(@RequestBody BookRentDto request) {
       
      return bookRentService.rentBook(request);
   }

   @PutMapping("/return/{rentId}")
   public ResponseEntity<MessageResponse> returnBook(@PathVariable Long rentId) {
      return bookRentService.returnBook(rentId);
   }

   @GetMapping("/all")
   public ResponseEntity<MessageResponse> getAllRents(@ModelAttribute CustomPageRequest customPageRequest) {
      return bookRentService.getAllRents(customPageRequest.getPage("id,asc"));
   }

   @GetMapping("/member/{memberId}")
   public ResponseEntity<MessageResponse> getRentsByMember(@PathVariable Long memberId) {
      return bookRentService.getRentsByMember(memberId);
   }
}
