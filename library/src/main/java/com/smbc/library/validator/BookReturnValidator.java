package com.smbc.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smbc.library.dto.BookReturnDto;
import com.smbc.library.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookReturnValidator implements Validator {

   private final ValidationUtil validationUtil;

   @Override
   public boolean supports(Class<?> clazz) {
      return BookReturnDto.class.equals(clazz);
   }

   @Override
   public void validate(Object target, Errors errors) {
      BookReturnDto bookReturnDto = (BookReturnDto) target;

      validationUtil.validateLongId(bookReturnDto.getBookRentId(), "bookRentId", "Book Rent Id", errors);
      validationUtil.validateIsbnList(bookReturnDto.getIsbn(), "isbn", "List ISBN", errors);
   }
}
