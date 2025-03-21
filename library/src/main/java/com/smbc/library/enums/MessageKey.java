package com.smbc.library.enums;


public enum MessageKey {
   ERROR_SERVER("error.server"),
   SUCCESS_SAVE_DATA("success.save.data"),
   SUCCESS_GET_DATA("success.get.data"),
   ERROR_NOT_FOUND("error.not.found"),
   EMAIL_DUPLICATE("email.duplicate"),
   ERROR_NO_CHANGE("error.no.change"),
   SUCCESS_UPDATE_DATA("success.update.data"),
   SUCCESS_DELETE_DATA("success.delete.data"),
   VALIDATION_ERROR("validation.errors"),
   VALIDATION_REQUIRED("validation.required"),
   VALIDATION_INVALID_FORMAT("validation.invalid_format"),
   ISBN_DUPLICATE("isbn.duplicate");

   private final String key;

   MessageKey(String key) {
       this.key = key;
   }

   public String getKey() {
       return key;
   }
}