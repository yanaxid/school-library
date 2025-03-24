package com.smbc.library.service;

import com.smbc.library.dto.BookRentDto;
import com.smbc.library.dto.BookReturnDto;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.enums.MessageKey;
import com.smbc.library.enums.RentStatus;
import com.smbc.library.model.Book;
import com.smbc.library.model.BookRent;
import com.smbc.library.model.BookRentDetail;
import com.smbc.library.model.Member;
import com.smbc.library.repository.BookRentRepository;
import com.smbc.library.repository.BookRepository;
import com.smbc.library.repository.MemberRepository;
import com.smbc.library.spesification.BookRentSpecification;
import com.smbc.library.util.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
      try {
         Optional<Member> memberOpt = memberRepository.findByIdAndDeletedFalse(request.getMemberId());
         if (memberOpt.isEmpty()) {
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), request.getMemberId().toString());
         }
         Member member = memberOpt.get();

         List<Book> books = request.getIsbn().stream()
               .map(bookRepository::findByIsbnAndDeletedFalse)
               .filter(Optional::isPresent)
               .map(Optional::get)
               .toList();

         if (books.isEmpty()) {
            return responseUtil.badRequest(MessageKey.ERROR_NOT_FOUND.getKey(), null, "Book");
         }

         List<String> outOfStockBooks = books.stream()
               .filter(book -> book.getStok() <= 0)
               .map(Book::getIsbn)
               .toList();

         if (!outOfStockBooks.isEmpty()) {
            return responseUtil.badRequest("Stok buku habis untuk ISBN: ", "null", outOfStockBooks.toString());
         }

         BookRent bookRent = BookRent.builder()
               .member(member)
               .rentDate(LocalDate.now())
               .dueDate(request.getDueDate())
               .status(RentStatus.DIPINJAM)
               .bookRentDetails(new ArrayList<>())
               .build();

         List<BookRentDetail> bookRentDetails = new ArrayList<>();
         for (Book book : books) {
            BookRentDetail detail = BookRentDetail.builder()
                  .bookRent(bookRent)
                  .book(book)
                  .status(RentStatus.DIPINJAM)
                  .build();
            bookRentDetails.add(detail);
            book.setStok(book.getStok() - 1);
         }

         bookRent.setBookRentDetails(bookRentDetails);

         bookRepository.saveAll(books);

         bookRentRepository.save(bookRent);
         return responseUtil.successWithData(MessageKey.SUCCESS_SAVE_DATA.getKey(), bookRent);
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // RETURN BOOK
   @Transactional
   public ResponseEntity<MessageResponse> returnBook(BookReturnDto bookReturnDto) {
      try {
         Optional<BookRent> bookRentOpt = bookRentRepository.findByIdAndDeletedFalse(bookReturnDto.getBookRentId());
         if (bookRentOpt.isEmpty()) {
            return responseUtil.badRequest(MessageKey.ERROR_NOT_FOUND.getKey(), null, "Bookrent");
         }
         BookRent bookRent = bookRentOpt.get();

         List<BookRentDetail> detailsToUpdate = bookRent.getBookRentDetails().stream()
               .filter(detail -> !detail.isDeleted())
               .filter(detail -> bookReturnDto.getIsbn().contains(detail.getBook().getIsbn()))
               .toList();

         if (detailsToUpdate.isEmpty()) {
            return responseUtil.badRequest(MessageKey.ERROR_NOT_FOUND.getKey(), null, "Book");
         }

         List<Book> booksToUpdate = new ArrayList<>();

         for (BookRentDetail detail : detailsToUpdate) {
            if (detail.getStatus() != RentStatus.DIKEMBALIKAN) {
               detail.setStatus(RentStatus.DIKEMBALIKAN);
               detail.setReturnDate(LocalDate.now());

               Book book = detail.getBook();
               book.setStok(book.getStok() + 1);
               booksToUpdate.add(book);
            }
         }

         boolean allReturned = bookRent.getBookRentDetails().stream()
               .filter(detail -> !detail.isDeleted())
               .allMatch(detail -> detail.getStatus() == RentStatus.DIKEMBALIKAN);

         if (allReturned) {
            bookRent.setReturnDate(LocalDate.now());
            bookRent.setStatus(RentStatus.DIKEMBALIKAN);
         }

         bookRepository.saveAll(booksToUpdate);
         bookRentRepository.save(bookRent);

         bookRent.setBookRentDetails(
               bookRent.getBookRentDetails().stream()
                     .filter(detail -> !detail.isDeleted())
                     .toList());

         return responseUtil.successWithData(MessageKey.SUCCESS_SAVE_DATA.getKey(), bookRent);

      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // GET ALL BOOK RENT
   public ResponseEntity<MessageResponse> getAllRent(String memberName, String bookTitle, RentStatus status,
         Pageable pageable) {
      try {
         Specification<BookRent> spec = BookRentSpecification.filterBy(memberName, bookTitle, status);
         Page<BookRent> rents = bookRentRepository.findAll(spec, pageable);

         if (rents.isEmpty()) {
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Bookrent");
         }

         List<BookRent> bookRentList = rents.getContent().stream()
               .map(bookRent -> {
                  List<BookRentDetail> activeDetails = bookRent.getBookRentDetails().stream()
                        .filter(detail -> !detail.isDeleted())
                        .toList();
                  bookRent.setBookRentDetails(activeDetails);
                  return bookRent;
               })
               .toList();

         MessageResponse.Meta meta = MessageResponse.Meta.builder()
               .total(rents.getTotalElements())
               .perPage(pageable.getPageSize())
               .currentPage(pageable.getPageNumber())
               .lastPage(rents.getTotalPages())
               .build();

         return responseUtil.successWithDataAndMeta(MessageKey.SUCCESS_GET_DATA.getKey(), bookRentList, meta);
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // GET BOOK RENT BY ID
   public ResponseEntity<MessageResponse> getRentById(Long bookRentId) {
      try {
         Optional<BookRent> bookRentOpt = bookRentRepository.findByIdAndDeletedFalse(bookRentId);
         if (bookRentOpt.isEmpty()) {
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Bookrent with ID " + bookRentId);
         }

         BookRent bookRent = bookRentOpt.get();

         List<BookRentDetail> activeDetails = bookRent.getBookRentDetails().stream()
               .filter(detail -> !detail.isDeleted())
               .toList();

         bookRent.setBookRentDetails(activeDetails);

         return responseUtil.successWithData(MessageKey.SUCCESS_GET_DATA.getKey(), bookRent);
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // DELETE
   @Transactional
   public ResponseEntity<MessageResponse> deleteBookRent(Long bookRentId) {
      try {
         Optional<BookRent> bookRentOpt = bookRentRepository.findByIdAndDeletedFalse(bookRentId);
         if (bookRentOpt.isEmpty()) {
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Bookrent ID " + bookRentId);
         }

         BookRent bookRent = bookRentOpt.get();

         boolean hasUnreturnedBooks = bookRent.getBookRentDetails().stream()
               .anyMatch(detail -> !detail.isDeleted() && detail.getStatus() == RentStatus.DIPINJAM);

         if (hasUnreturnedBooks) {
            return responseUtil.badRequest(MessageKey.ERROR_FAILED_DELETED.getKey(), "There is book stil borowed");
         }

         for (BookRentDetail detail : bookRent.getBookRentDetails()) {
            detail.setDeleted(true);
         }

         bookRent.setDeleted(true);
         bookRentRepository.save(bookRent);

         return responseUtil.success(MessageKey.SUCCESS_DELETE_DATA.getKey(), "rentbook id :: " + bookRentId);

      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

}
