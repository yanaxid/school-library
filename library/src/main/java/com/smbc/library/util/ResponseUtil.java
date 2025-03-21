package com.smbc.library.util;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.smbc.library.dto.response.MessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseUtil {

   private final MessageUtil messageUtil;

   private ResponseEntity<MessageResponse> buildResponse(
         HttpStatus status,
         String messageKey,
         Object data,
         Object error,
         MessageResponse.Meta meta,
         String... args) {

      String message = messageUtil.get(messageKey, (Object[]) args);
      return ResponseEntity.status(status).body(new MessageResponse(
            message,
            status.value(),
            status.getReasonPhrase(),
            data,
            error,
            meta));
   }


   public ResponseEntity<MessageResponse> notFound(String messageKey, String... args) {
      return buildResponse(HttpStatus.NOT_FOUND, messageKey, null, null, null, args);
   }


   public ResponseEntity<MessageResponse> successWithData(String messageKey, Object data , String...args ) {
      return buildResponse(HttpStatus.OK, messageKey, data, null, null, args);
   }


   public ResponseEntity<MessageResponse> success(String messageKey, String... args) {
      return buildResponse(HttpStatus.OK, messageKey, null,null, null, args);
   }

   public ResponseEntity<MessageResponse> successWithDataAndMeta(String messageKey, Object data,
         MessageResponse.Meta meta) {
      return buildResponse(HttpStatus.OK, messageKey, data, null, meta);
   }

   public ResponseEntity<MessageResponse> badRequest(String messageKey, Object error, String... args) {
      return buildResponse(HttpStatus.BAD_REQUEST, messageKey, null, error, null, args);
   }

   public ResponseEntity<MessageResponse> internalServerError(String messageKey) {
      return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageKey, null, null, null);
   }
}
