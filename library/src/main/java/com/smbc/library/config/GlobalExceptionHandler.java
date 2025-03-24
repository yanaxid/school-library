package com.smbc.library.config;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.smbc.library.enums.MessageKey;
import com.smbc.library.util.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import com.smbc.library.dto.response.MessageResponse;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

   private final ResponseUtil responseUtil;

   // untuk validasi dto field misalnya
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

      return responseUtil.badRequest(
            MessageKey.VALIDATION_INVALID_FORMAT.getKey(),
            List.of(ex.getMessage()));
   }

   // untuk validasi params
   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<MessageResponse> handleConstraintViolationException(ConstraintViolationException ex) {
      return responseUtil.badRequest(
            MessageKey.VALIDATION_INVALID_FORMAT.getKey(),
            List.of(ex.getMessage()));
   }

   @ExceptionHandler(EntityNotFoundException.class)
   public ResponseEntity<MessageResponse> handleEntityNotFoundException(EntityNotFoundException ex) {

      return responseUtil.badRequest(
            MessageKey.ERROR_NOT_FOUND.getKey(),
            List.of(ex.getMessage()));
   }

   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

      return responseUtil.badRequest(
            MessageKey.VALIDATION_INVALID_FORMAT.getKey(),
            List.of(ex.getMessage()));
   }

   // general error
   @ExceptionHandler(Exception.class)
   public ResponseEntity<MessageResponse> handleGeneralException(Exception ex) {
      return responseUtil.badRequest(
            MessageKey.ERROR_GENERAL.getKey(),
            List.of(ex.getMessage()));
   }

   // format json salah
   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ResponseEntity<MessageResponse> handleJsonParseException(HttpMessageNotReadableException ex) {

      return responseUtil.badRequest(
            MessageKey.VALIDATION_INVALID_FORMAT.getKey(),
            List.of(ex.getMostSpecificCause().getMessage()));
   }

   // parameter salah tipe
   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException(
         MethodArgumentTypeMismatchException ex) {

      return responseUtil.badRequest(
            MessageKey.VALIDATION_INVALID_FORMAT.getKey(),
            List.of(ex.getMostSpecificCause().getMessage()));
   }

}
