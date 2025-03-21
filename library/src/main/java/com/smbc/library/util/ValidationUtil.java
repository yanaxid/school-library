package com.smbc.library.util;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.validation.Validator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.enums.MessageKey;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

   private final ResponseUtil responseUtil;

   private ResponseEntity<MessageResponse> validate(Object request, Validator validator, ResponseUtil responseUtil) {
      BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
      validator.validate(request, bindingResult);

      if (bindingResult.hasErrors()) {
         List<String> errors = bindingResult.getFieldErrors().stream()
               .map(error -> error.getDefaultMessage())
               .toList();

         return responseUtil.badRequest(MessageKey.VALIDATION_ERROR.getKey(), errors);
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
}
