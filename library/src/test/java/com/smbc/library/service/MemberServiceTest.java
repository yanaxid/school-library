package com.smbc.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.smbc.library.dto.MemberDto;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.enums.MessageKey;
import com.smbc.library.model.Member;
import com.smbc.library.repository.MemberRepository;
import com.smbc.library.util.ResponseUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

   @Mock
   private MemberRepository memberRepository;

   @Mock
   private ResponseUtil responseUtil;

   @InjectMocks
   private MemberService memberService;

   private Member member;
   private MemberDto memberDto;

   @BeforeEach
   void setUp() {

      memberDto = new MemberDto("yana", "badnung", "yana@mail.com");

      member = Member.builder()
            .id(1L)
            .nama("yana")
            .alamat("bandung")
            .email("yana@mail.com")
            .deleted(false)
            .build();
   }

   // SUCCESS GET MEMBER
   @Test
   void getMember_Success() {
      Long memberId = 1L;

      when(memberRepository.findByIdAndDeletedFalse(memberId))
            .thenReturn(Optional.of(member));

      when(responseUtil.successWithData(anyString(), any()))
            .thenReturn(ResponseEntity.ok(new MessageResponse("Success", 200, "OK", member, null, null)));

      ResponseEntity<MessageResponse> response = memberService.getMember(memberId);

      assertNotNull(response, "Response should not be null");
      assertEquals(200, response.getStatusCode().value());
      assertEquals("Success", response.getBody().getMessage());

      verify(memberRepository, times(1)).findByIdAndDeletedFalse(memberId);
   }

   // MEMBER NOT FOUND
   @Test
   void getMember_NotFound() {
      Long memberId = 1L;

      when(memberRepository.findByIdAndDeletedFalse(memberId))
            .thenReturn(Optional.empty());

      when(responseUtil.notFound(eq(MessageKey.ERROR_NOT_FOUND.getKey()), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(new MessageResponse("Not Found", 404, "Not Found", null, null, null)));

      ResponseEntity<MessageResponse> response = memberService.getMember(memberId);

      assertNotNull(response, "Response should not be null");
      assertEquals(404, response.getStatusCode().value());
      assertEquals("Not Found", response.getBody().getMessage());

      verify(responseUtil, times(1)).notFound(eq(MessageKey.ERROR_NOT_FOUND.getKey()), anyString());
      verify(memberRepository, times(1)).findByIdAndDeletedFalse(memberId);
   }

   // SERVER ERROR
   @Test
   void getMember_InternalServerError() {
      Long memberId = 1L;

      when(memberRepository.findByIdAndDeletedFalse(memberId))
            .thenThrow(new RuntimeException("Database Error"));

      when(responseUtil.internalServerError(anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new MessageResponse("Internal Server Error", 500, "Internal Server Error", null, null, null)));

      ResponseEntity<MessageResponse> response = memberService.getMember(memberId);

      assertNotNull(response, "Response should not be null");
      assertEquals(500, response.getStatusCode().value());
      assertEquals("Internal Server Error", response.getBody().getMessage());

      verify(memberRepository, times(1)).findByIdAndDeletedFalse(memberId);
   }

   // IF MEMBER FOUND BUT HAS DELETED
   @Test
   void getMember_DeletedMember() {
      Long memberId = 1L;
      member.setDeleted(true);

      when(memberRepository.findByIdAndDeletedFalse(memberId))
            .thenReturn(Optional.empty());

      when(responseUtil.notFound(eq(MessageKey.ERROR_NOT_FOUND.getKey()), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(new MessageResponse("Member not found", 404, "Not Found", null, null, null)));

      ResponseEntity<MessageResponse> response = memberService.getMember(memberId);

      assertNotNull(response, "Response should not be null");
      assertEquals(404, response.getStatusCode().value());
      assertEquals("Member not found", response.getBody().getMessage());

      verify(responseUtil, times(1)).notFound(eq(MessageKey.ERROR_NOT_FOUND.getKey()), anyString());
      verify(memberRepository, times(1)).findByIdAndDeletedFalse(memberId);
   }

   // SUCCESS ADD MEMBER
   @Test
   void addMember_Success() {
      when(memberRepository.findByEmailAndDeletedFalse(memberDto.getEmail()))
            .thenReturn(Optional.empty());

      when(memberRepository.save(any(Member.class)))
            .thenReturn(member);

      when(responseUtil.successWithData(eq(MessageKey.SUCCESS_SAVE_DATA.getKey()), any(Member.class)))
            .thenReturn(ResponseEntity.ok(new MessageResponse("Success", 200, "OK", member, null, null)));

      ResponseEntity<MessageResponse> response = memberService.addMember(memberDto);

      assertNotNull(response, "Response should not be null");
      assertEquals(200, response.getStatusCode().value());
      assertEquals("Success", response.getBody().getMessage());

      verify(memberRepository, times(1)).findByEmailAndDeletedFalse(memberDto.getEmail());
      verify(memberRepository, times(1)).save(any(Member.class));
   }

   // DUPLICATE EMAIL ERROR
   @Test
   void addMember_EmailAlreadyExists() {
      when(memberRepository.findByEmailAndDeletedFalse(memberDto.getEmail()))
            .thenReturn(Optional.of(member));

      when(responseUtil.badRequest(eq(MessageKey.EMAIL_DUPLICATE.getKey()), any(), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                  .body(new MessageResponse("Email already exists", 400, "Bad Request", null, null, null)));

      ResponseEntity<MessageResponse> response = memberService.addMember(memberDto);

      assertNotNull(response, "Response should not be null");
      assertEquals(400, response.getStatusCode().value());
      assertEquals("Email already exists", response.getBody().getMessage());

      verify(memberRepository, times(1)).findByEmailAndDeletedFalse(memberDto.getEmail());
      verify(memberRepository, times(0)).save(any(Member.class)); // jangan simpan member
   }

   // SERVER ERROR
   @Test
   void addMember_InternalServerError() {
      when(memberRepository.findByEmailAndDeletedFalse(memberDto.getEmail()))
            .thenReturn(Optional.empty());

      when(memberRepository.save(any(Member.class)))
            .thenThrow(new RuntimeException("Database error"));

      when(responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey()))
            .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new MessageResponse("Internal Server Error", 500, "Internal Server Error", null, null, null)));

      ResponseEntity<MessageResponse> response = memberService.addMember(memberDto);

      assertNotNull(response, "Response should not be null");
      assertEquals(500, response.getStatusCode().value());
      assertEquals("Internal Server Error", response.getBody().getMessage());

      verify(memberRepository, times(1)).findByEmailAndDeletedFalse(memberDto.getEmail());
      verify(memberRepository, times(1)).save(any(Member.class));
   }
}
