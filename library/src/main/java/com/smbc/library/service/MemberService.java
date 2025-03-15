package com.smbc.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smbc.library.dto.MemberDto;
import com.smbc.library.dto.response.MessageResponse;
import com.smbc.library.model.Member;
import com.smbc.library.repository.MemberRepository;
import com.smbc.library.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

   private final MemberRepository memberRepository;
   private final ResponseUtil responseUtil;

   // ADD MEMBER
   @Transactional
   public ResponseEntity<MessageResponse> addMember(MemberDto request) {
      try {
         Member member = Member.builder()
               .nama(request.getNama())
               .alamat(request.getAlamat())
               .email(request.getEmail())
               .build();

         memberRepository.save(member);

         MemberDto memberResponse = MemberDto.builder()
               .nama(member.getNama())
               .alamat(member.getAlamat())
               .email(member.getEmail())
               .build();

         return responseUtil.okWithData("success.save.data", memberResponse);
      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }

   // GET ALL MEMBER
   public ResponseEntity<MessageResponse> getAllMembers(Pageable pageable) {
      try {
         Page<Member> members = memberRepository.findByDeletedFalse(pageable);

         if (members.isEmpty()) {
            return responseUtil.notFound("error.not.found");
         }

         List<Member> memberList = members.getContent();

         MessageResponse.Meta meta = MessageResponse.Meta.builder()
               .total(members.getTotalElements())
               .perPage(pageable.getPageSize())
               .currentPage(pageable.getPageNumber())
               .lastPage(members.getTotalPages())
               .build();

         return responseUtil.okWithDataAndMeta("success.get.data", memberList, meta);
      } catch (Exception e) {
         return responseUtil.internalServerError("error.server: " + e.getMessage());
      }
   }

   // GET MEMBER
   public ResponseEntity<MessageResponse> getMember(Long id) {
      try {
         Optional<Member> memberOpt = memberRepository.findByIdAndDeletedFalse(id);

         if (memberOpt.isEmpty()) {
            return responseUtil.notFound("error.not.found");
         }

         return responseUtil.okWithData("success.get.data", memberOpt.get());
      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }

   // UPDATE MEMBER
   @Transactional
   public ResponseEntity<MessageResponse> updateMember(Long id, MemberDto request) {
      try {
         Optional<Member> memberData = memberRepository.findById(id);
         if (memberData.isPresent()) {
            Member member = memberData.get();
            member.setNama(request.getNama());
            member.setAlamat(request.getAlamat());
            member.setEmail(request.getEmail());
            memberRepository.save(member);
            return responseUtil.okWithData("success.update.data", member);
         } else {
            return responseUtil.notFound("error.not.found");
         }
      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }

   // DELETE MEMBER
   @Transactional
   public ResponseEntity<MessageResponse> deleteMember(Long id) {
      try {
         Optional<Member> memberOpt = memberRepository.findByIdAndDeletedFalse(id);

         if (memberOpt.isEmpty()) {
            return responseUtil.notFound("error.not.found");
         }

         Member member = memberOpt.get();
         member.setDeleted(true);
         memberRepository.save(member);

         return responseUtil.ok("success.delete.data");
      } catch (Exception e) {
         return responseUtil.internalServerError("error.server");
      }
   }
}
