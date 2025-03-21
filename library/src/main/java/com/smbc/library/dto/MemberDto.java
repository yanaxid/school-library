package com.smbc.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor

public class MemberDto {
   
   private String nama;
   private String alamat;
   private String email;
   
}
