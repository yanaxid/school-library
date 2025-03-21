package com.smbc.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smbc.library.dto.BookDto;
import com.smbc.library.enums.MessageKey;
import com.smbc.library.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookValidator implements Validator {

   private final MessageUtil messageUtil;

   @Override
   public boolean supports(Class<?> clazz) {
      return BookDto.class.equals(clazz);
   }

   @Override
   public void validate(Object target, Errors errors) {
      BookDto book = (BookDto) target;

      // Validasi string yang harus diisi dan tidak boleh angka
      validateStringField(book.getJudul(), "judul", "Judul", errors);
      validateStringField(book.getPenulis(), "penulis", "Penulis", errors);
      validateStringField(book.getPenerbit(), "penerbit", "Penerbit", errors);
      validateStringField2(book.getIsbn(), "isbn", "ISBN", errors);
      validateStringField(book.getKategori(), "kategori", "Kategori", errors);

      // Validasi angka wajib diisi
      validateNumericField(book.getTahunTerbit(), "tahun_terbit", "Tahun Terbit", errors);
   }

   private void validateStringField(String fieldValue, String fieldName, String displayName, Errors errors) {
      if (isNullOrEmpty(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else if (isNumeric(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName);
      }
   }

   private void validateStringField2(String fieldValue, String fieldName, String displayName, Errors errors) {
      if (isNullOrEmpty(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      }
   }

   private void validateNumericField(Integer fieldValue, String fieldName, String displayName, Errors errors) {
      if (fieldValue == null) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      }
   }

   private boolean isNullOrEmpty(String value) {
      return value == null || value.trim().isEmpty();
   }

   private boolean isNumeric(String value) {
      return value.matches("\\d+");
   }

   private void reject(Errors errors, String fieldName, MessageKey key, String displayName) {
      errors.rejectValue(
            fieldName,
            key.getKey(),
            messageUtil.get(key.getKey(), displayName));
   }
}
