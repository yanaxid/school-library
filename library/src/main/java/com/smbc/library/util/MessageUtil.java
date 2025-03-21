package com.smbc.library.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageUtil {

   private final MessageSource messageSource;


   public String get(String code, Object... args) {
      LocaleContextHolder.setLocale(new Locale("en"));
      Locale locale = LocaleContextHolder.getLocale();
      return getMessage(code, locale, args);
   }

   public String get(Locale locale, String code, Object... args) {
      return getMessage(code, locale, args);
   }

   private String getMessage(String code, Locale locale, Object... args) {
      return messageSource.getMessage(code, args, code, locale);
   }
}
