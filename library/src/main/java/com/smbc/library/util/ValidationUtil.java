package com.smbc.library.util;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.validation.Validator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.enums.MessageKey;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

   private final ResponseUtil responseUtil;
   private final MessageUtil messageUtil;
   private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

   private ResponseEntity<MessageResponse> validate(Object request, Validator validator, ResponseUtil responseUtil) {
      BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
      validator.validate(request, bindingResult);

      if (bindingResult.hasErrors()) {
         List<String> errors = bindingResult.getFieldErrors().stream()
               .map(error -> error.getDefaultMessage())
               .toList();

         return responseUtil.badRequest(MessageKey.VALIDATION_INVALID_FORMAT.getKey(), errors);
      }

      return null;
   }

   public ResponseEntity<MessageResponse> processWithValidation(
         Object request,
         Validator validator,
         Supplier<ResponseEntity<MessageResponse>> action) {

      ResponseEntity<MessageResponse> validationResponse = validate(request, validator, responseUtil);
      return (validationResponse != null) ? validationResponse : action.get();
   }

   private void reject(Errors errors, String fieldName, MessageKey key, String displayName) {
      errors.rejectValue(
            fieldName,
            key.getKey(),
            messageUtil.get(key.getKey(), displayName));
   }

   private boolean isNullOrEmpty(String value) {
      return value == null || value.trim().isEmpty();
   }

   private boolean isNumeric(String value) {
      return value.matches("\\d+");
   }

   public void validateStringField(String fieldValue, String fieldName, String displayName, Errors errors, int min,
         int max) {
      if (isNullOrEmpty(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else if (isNumeric(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName);
      } else if (fieldValue.length() < min || fieldValue.length() > max) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_LENGTH, displayName);
      }
   }

   public void validateEmail(String email, String fieldName, String displayName, Errors errors) {
      if (isNullOrEmpty(email)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else if (!email.matches(EMAIL_PATTERN)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName);
      }
   }

   public void validateNumericField(Integer fieldValue, String fieldName, String displayName, Errors errors) {
      if (fieldValue == null) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      }
   }

   public void validateISBN(String fieldValue, String fieldName, String displayName, Errors errors) {
      if (isNullOrEmpty(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      }
   }

   public void validateIsbnList(List<String> isbnList, String fieldName, String displayName, Errors errors) {
      if (isbnList == null || isbnList.isEmpty()) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else {
         for (int i = 0; i < isbnList.size(); i++) {
            validateISBN(isbnList.get(i), fieldName + "[" + i + "]", "ISBN", errors);
         }
      }
   }

   public void validateLongId(Long id, String fieldName, String displayName, Errors errors) {
      if (id == null || id <= 0) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName);
      }
   }

   public void validateDueDate(LocalDate dueDate, String fieldName, String displayName, Errors errors) {
      LocalDate today = LocalDate.now();
      LocalDate maxDate = today.plusYears(1);

      if (dueDate == null) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else if (dueDate.isBefore(today)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName + " cannot be in the past.");
      } else if (dueDate.isAfter(maxDate)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT,
               displayName + " cannot be more than 1 year ahead.");
      }
   }

}
