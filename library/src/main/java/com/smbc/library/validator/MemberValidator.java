package com.smbc.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smbc.library.dto.MemberDto;
import com.smbc.library.enums.MessageKey;
import com.smbc.library.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator implements Validator {

   private final MessageUtil messageUtil;
   private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

   @Override
   public boolean supports(Class<?> clazz) {
      return MemberDto.class.equals(clazz);
   }

   @Override
   public void validate(Object target, Errors errors) {
      MemberDto member = (MemberDto) target;

      // Validasi string yang harus diisi dan tidak boleh angka
      validateStringField(member.getNama(), "nama", "Nama", errors);
      validateStringField(member.getAlamat(), "alamat", "Alamat", errors);
      validateEmail(member.getEmail(), "email", "Email", errors);
   }

   private void validateStringField(String fieldValue, String fieldName, String displayName, Errors errors) {
      if (isNullOrEmpty(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else if (isNumeric(fieldValue)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName);
      }
   }

   private void validateEmail(String email, String fieldName, String displayName, Errors errors) {
      if (isNullOrEmpty(email)) {
         reject(errors, fieldName, MessageKey.VALIDATION_REQUIRED, displayName);
      } else if (!email.matches(EMAIL_PATTERN)) {
         reject(errors, fieldName, MessageKey.VALIDATION_INVALID_FORMAT, displayName);
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
