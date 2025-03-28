package com.smbc.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smbc.library.dto.MemberDto;
import com.smbc.library.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator implements Validator {

   private final ValidationUtil validationUtil;

   @Override
   public boolean supports(Class<?> clazz) {
      return MemberDto.class.equals(clazz);
   }

   @Override
   public void validate(Object target, Errors errors) {
      MemberDto member = (MemberDto) target;

      validationUtil.validateStringField(member.getNama(), "nama", "Nama", errors, 1 , 100);
      validationUtil.validateStringField(member.getAlamat(), "alamat", "Alamat", errors, 1 , 100);
      validationUtil.validateEmail(member.getEmail(), "email", "Email", errors);
   }
}
