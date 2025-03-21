package com.smbc.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smbc.library.dto.MemberDto;
import com.smbc.library.dto.request.CustomPageRequest;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.service.MemberService;
import com.smbc.library.util.ValidationUtil;
import com.smbc.library.validator.MemberValidator;

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
public class MemberController {

   private final MemberService memberService;
   private final MemberValidator memberValidator;
   private final ValidationUtil validationUtil;

   @PostMapping("/add")
   public ResponseEntity<MessageResponse> addMember(@RequestBody MemberDto request) {
      return validationUtil.processWithValidation(request, memberValidator, () -> memberService.addMember(request));
   }

   @GetMapping("/all")
   public ResponseEntity<MessageResponse> getAllMembers(@ModelAttribute CustomPageRequest customPageRequest) {
      return memberService.getAllMembers(customPageRequest.getPage("nama,asc"));
   }

   @GetMapping("/{id}")
   public ResponseEntity<MessageResponse> getMember(@PathVariable Long id) {
      return memberService.getMember(id);
   }

   @PutMapping("/update/{id}")
   public ResponseEntity<MessageResponse> updateMember(@PathVariable Long id, @RequestBody MemberDto request) {
      return validationUtil.processWithValidation(request, memberValidator, () -> memberService.updateMember(id, request));
   }

   @PutMapping("/delete/{id}")
   public ResponseEntity<MessageResponse> deleteMember(@PathVariable Long id) {
      return memberService.deleteMember(id);
   }
}
