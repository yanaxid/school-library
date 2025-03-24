package com.smbc.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smbc.library.dto.BookDto;
import com.smbc.library.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookValidator implements Validator {

   private final ValidationUtil validationUtil;

   @Override
   public boolean supports(Class<?> clazz) {
      return BookDto.class.equals(clazz);
   }

   @Override
   public void validate(Object target, Errors errors) {
      BookDto book = (BookDto) target;

      validationUtil.validateStringField(book.getJudul(), "judul", "Judul", errors, 1 , 100);
      validationUtil.validateStringField(book.getPenulis(), "penulis", "Penulis", errors, 1 , 100);
      validationUtil.validateStringField(book.getPenerbit(), "penerbit", "Penerbit", errors, 1 , 100);
      validationUtil.validateISBN(book.getIsbn(), "isbn", "ISBN", errors);
      validationUtil.validateStringField(book.getKategori(), "kategori", "Kategori", errors, 1 , 100);
      validationUtil.validateNumericField(book.getTahunTerbit(), "tahun_terbit", "Tahun Terbit", errors);
   }
}
