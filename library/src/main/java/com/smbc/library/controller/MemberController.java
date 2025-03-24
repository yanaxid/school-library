package com.smbc.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smbc.library.dto.MemberDto;
import com.smbc.library.dto.request.CustomPageRequest;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.service.MemberService;
import com.smbc.library.util.ValidationUtil;
import com.smbc.library.validator.MemberValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "API untuk mengelola data member perpustakaan.")
public class MemberController {

   private final MemberService memberService;
   private final MemberValidator memberValidator;
   private final ValidationUtil validationUtil;

   @Operation(
      summary = "Tambah Member",
      description = "Endpoint untuk menambahkan member baru"
   )
   @PostMapping("/add")
   public ResponseEntity<MessageResponse> addMember(@RequestBody MemberDto request) {
      return validationUtil.processWithValidation(request, memberValidator, () -> memberService.addMember(request));
   }

   @Operation(
      summary = "Dapatkan Semua Member",
      description = "Mengambil daftar semua member perpustakaan"
   )
   @GetMapping("/all")
   public ResponseEntity<MessageResponse> getAllMembers(@ModelAttribute CustomPageRequest customPageRequest) {
      return memberService.getAllMembers(customPageRequest.getPage("nama,asc"));
   }

   @Operation(
      summary = "Dapatkan Detail Member",
      description = "Mendapatkan informasi detail member berdasarkan ID member"
   )
   @GetMapping("/{id}")
   public ResponseEntity<MessageResponse> getMember(@PathVariable @Min(0) Long id) {
      return memberService.getMember(id);
   }

   @Operation(
      summary = "Perbarui Data Member",
      description = "Memperbarui informasi member berdasarkan ID member"
   )
   @PutMapping("/update/{id}")
   public ResponseEntity<MessageResponse> updateMember(@PathVariable @Min(0) Long id, @RequestBody MemberDto request) {
      return validationUtil.processWithValidation(request, memberValidator, () -> memberService.updateMember(id, request));
   }

   @Operation(
      summary = "Hapus Member",
      description = "Menghapus member dari sistem perpustakaan berdasarkan ID"
   )
   @PutMapping("/delete/{id}")
   public ResponseEntity<MessageResponse> deleteMember(@PathVariable @Min(0) Long id) {
      return memberService.deleteMember(id);
   }
}
