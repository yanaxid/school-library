package com.smbc.library.service;

import com.smbc.library.dto.BookRentDto;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.model.Book;
import com.smbc.library.model.BookRent;
import com.smbc.library.model.Member;
import com.smbc.library.repository.BookRentRepository;
import com.smbc.library.repository.BookRepository;
import com.smbc.library.repository.MemberRepository;
import com.smbc.library.util.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookRentService {

   private final BookRentRepository bookRentRepository;
   private final BookRepository bookRepository;
   private final MemberRepository memberRepository;
   private final ResponseUtil responseUtil;

   // RENT BOOK
   @Transactional
   public ResponseEntity<MessageResponse> rentBook(BookRentDto request) {
      Optional<Book> book = bookRepository.findById(request.getBookId());
      Optional<Member> member = memberRepository.findById(request.getMemberId());

      if (book.isEmpty()) {
         return responseUtil.notFound("Book not found");
      }
      if (member.isEmpty()) {
         return responseUtil.notFound("Member not found");
      }

      BookRent bookRent = BookRent.builder()
            .book(book.get())
            .member(member.get())
            .rentDate(LocalDate.now())
            .dueDate(request.getDueDate())
            .status("Dipinjam")
            .build();

      bookRentRepository.save(bookRent);
      return responseUtil.success("Book successfully rented");
   }

   // RETURN BOOK
   @Transactional
   public ResponseEntity<MessageResponse> returnBook(Long rentId) {
      log.info("Received rentId: " + rentId);

      Optional<BookRent> bookRent = bookRentRepository.findById(rentId);
      if (bookRent.isEmpty()) {
         log.info("BookRent record not found for ID: " + rentId);
         return responseUtil.notFound("BookRent record not found");
      }

      BookRent rent = bookRent.get();
      log.info("BookRent found: " + rent);

      rent.setReturnDate(LocalDate.now());

      if (rent.getReturnDate().isAfter(rent.getDueDate())) {
         rent.setStatus("Terlambat");
      } else {
         rent.setStatus("Dikembalikan");
      }

      bookRentRepository.save(rent);
      return responseUtil.success("Book successfully returned");
   }

   // GET ALL RENTS
   public ResponseEntity<MessageResponse> getAllRents(Pageable pageable) {
      try {
          Page<BookRent> rents = bookRentRepository.findAll(pageable); // Asumsi Anda ingin mengambil semua, jika perlu filter deleted=false, sesuaikan dengan logic Book

          if (rents.isEmpty()) {
              return responseUtil.notFound("error.not.found");
          }

          List<BookRent> rentList = rents.getContent();

          MessageResponse.Meta meta = MessageResponse.Meta.builder()
                  .total(rents.getTotalElements())
                  .perPage(pageable.getPageSize())
                  .currentPage(pageable.getPageNumber() + 1)
                  .lastPage(rents.getTotalPages())
                  .build();

          return responseUtil.successWithDataAndMeta("success.get.data", rentList, meta);

      } catch (Exception e) {
          return responseUtil.internalServerError("error.server");
      }

   }

   // GET MEMBER RENT
   public ResponseEntity<MessageResponse> getRentsByMember(Long memberId) {
      List<BookRent> rents = bookRentRepository.findByMemberId(memberId);
      return responseUtil.successWithData("Success", rents);
   }
}
