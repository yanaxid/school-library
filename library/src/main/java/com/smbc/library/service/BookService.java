package com.smbc.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smbc.library.dto.BookDto;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.enums.MessageKey;
import com.smbc.library.model.Book;
import com.smbc.library.repository.BookRepository;
import com.smbc.library.util.ResponseUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

   private final BookRepository bookRepository;
   private final ResponseUtil responseUtil;

   // ADD BOOK
   @Transactional
   public ResponseEntity<MessageResponse> addBook(BookDto request) {
      try {

         if (bookRepository.findByIsbnAndDeletedFalse(request.getIsbn()).isPresent()) {
            return responseUtil.badRequest(MessageKey.ISBN_DUPLICATE.getKey(),null, request.getIsbn());
         }

         Book book = Book.builder()
               .judul(request.getJudul())
               .penulis(request.getPenulis())
               .penerbit(request.getPenerbit())
               .tahunTerbit(request.getTahunTerbit())
               .isbn(request.getIsbn())
               .kategori(request.getKategori())
               .deleted(false)
               .build();

         bookRepository.save(book);

         return responseUtil.successWithData(MessageKey.SUCCESS_SAVE_DATA.getKey(), book);

      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // GET ALL BOOKS
   public ResponseEntity<MessageResponse> getAllBooks(Pageable pageable) {
      try {

         Page<Book> books = bookRepository.findByDeletedFalse(pageable);

         if (books.isEmpty())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Book");

         List<Book> bookList = books.getContent();

         MessageResponse.Meta meta = MessageResponse.Meta.builder()
               .total(books.getTotalElements())
               .perPage(pageable.getPageSize())
               .currentPage(pageable.getPageNumber() + 1)
               .lastPage(books.getTotalPages())
               .build();

         return responseUtil.successWithDataAndMeta(MessageKey.SUCCESS_GET_DATA.getKey(), bookList, meta);

      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // GET BOOK
   public ResponseEntity<MessageResponse> getBook(String isbn) {
      try {
         Optional<Book> bookOpt = bookRepository.findByIsbnAndDeletedFalse(isbn);

         if (bookOpt.isEmpty())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Book with isbn " + isbn);

         return responseUtil.successWithData(MessageKey.SUCCESS_GET_DATA.getKey(), bookOpt.get());
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // UPDATE BOOK
   @Transactional
   public ResponseEntity<MessageResponse> updateBook(Long id, BookDto request) {
      try {

         // chek is id exist
         Optional<Book> bookOpt = bookRepository.findById(id);
         if (bookOpt.isEmpty() || bookOpt.get().isDeleted())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), id.toString());

         // isbn should not duplicate
         if (!bookOpt.get().getId().equals(id)) {
            return responseUtil.badRequest(MessageKey.EMAIL_DUPLICATE.getKey(), null, request.getIsbn());
         }

         Book book = bookOpt.get();

         // chek no change
         if (book.getJudul().equals(request.getJudul()) &&
               book.getPenulis().equals(request.getPenulis()) &&
               book.getPenerbit().equals(request.getPenerbit()) &&
               book.getIsbn().equals(request.getIsbn()) &&
               book.getKategori().equals(request.getKategori()) &&
               book.getTahunTerbit().equals(request.getTahunTerbit())) {
            return responseUtil.badRequest(MessageKey.ERROR_NO_CHANGE.getKey(), null);
         }

         book.setJudul(request.getJudul());
         book.setPenulis(request.getPenulis());
         book.setPenerbit(request.getPenerbit());
         book.setTahunTerbit(request.getTahunTerbit());
         book.setIsbn(request.getIsbn());
         book.setKategori(request.getKategori());
         bookRepository.save(book);

         return responseUtil.successWithData(MessageKey.SUCCESS_UPDATE_DATA.getKey(), book,
               "Book with title " + book.getJudul());
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // DELETE BOK
   @Transactional
   public ResponseEntity<MessageResponse> deleteBook(Long id) {
      try {
         Optional<Book> bookOptional = bookRepository.findById(id);

         if (bookOptional.isEmpty() || bookOptional.get().isDeleted())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), id.toString());

         Book book = bookOptional.get();
         book.setDeleted(true);
         bookRepository.save(book);

         return responseUtil.success(MessageKey.SUCCESS_DELETE_DATA.getKey(), "Book with id " + id.toString());

      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }
}
