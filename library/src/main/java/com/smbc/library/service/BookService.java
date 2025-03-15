package com.smbc.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smbc.library.dto.BookDto;
import com.smbc.library.dto.response.MessageResponse;
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
            return responseUtil.badRequest("error.not.found");
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

         return responseUtil.ok("success.add.data");

      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }

   // GET ALL BOOKS
   public ResponseEntity<MessageResponse> getAllBooks(Pageable pageable) {
      try {
         Page<Book> books = bookRepository.findByDeletedFalse(pageable);

         if (books.isEmpty()) {
            return responseUtil.notFound("error.not.found");
         }

         List<Book> bookList = books.getContent();

         MessageResponse.Meta meta = MessageResponse.Meta.builder()
               .total(books.getTotalElements())
               .perPage(pageable.getPageSize())
               .currentPage(pageable.getPageNumber() + 1)
               .lastPage(books.getTotalPages())
               .build();

         return responseUtil.okWithDataAndMeta("success.get.data", bookList, meta);

      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }



   //UPDATE BOOK
   @Transactional
   public ResponseEntity<MessageResponse> updateBook(Long id, Book request) {
      try {
         Book book = bookRepository.findById(id).orElse(null);

         if (book == null || book.isDeleted()) {
            return responseUtil.notFound("error.not.found");
         }

         book.setJudul(request.getJudul());
         book.setPenulis(request.getPenulis());
         book.setPenerbit(request.getPenerbit());
         book.setTahunTerbit(request.getTahunTerbit());
         book.setIsbn(request.getIsbn());
         book.setKategori(request.getKategori());
         bookRepository.save(book);

         return responseUtil.ok("success.update.data");
      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }

   // DELETE BOK
   @Transactional
   public ResponseEntity<MessageResponse> deleteBook(Long id) {
      try {
         Optional<Book> bookOptional = bookRepository.findById(id);

         if (bookOptional.isEmpty() || bookOptional.get().isDeleted()) {
            return responseUtil.notFound("error.not.found");
         }

         Book book = bookOptional.get();
         book.setDeleted(true);
         bookRepository.save(book);

         return responseUtil.ok("success.delete.data");

      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }
}
