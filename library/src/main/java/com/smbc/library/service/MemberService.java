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
import com.smbc.library.enums.MessageKey;
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

         // cek duplicate email
         if (memberRepository.findByEmailAndDeletedFalse(request.getEmail()).isPresent())
            return responseUtil.badRequest(MessageKey.EMAIL_DUPLICATE.getKey(), null, request.getEmail());

         Member member = Member.builder()
               .nama(request.getNama())
               .alamat(request.getAlamat())
               .email(request.getEmail())
               .build();

         memberRepository.save(member);

         return responseUtil.successWithData(MessageKey.SUCCESS_SAVE_DATA.getKey(), member);
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // GET ALL MEMBER
   public ResponseEntity<MessageResponse> getAllMembers(Pageable pageable) {
      try {

         Page<Member> members = memberRepository.findByDeletedFalse(pageable);

         if (members.isEmpty())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Member");

         List<Member> memberList = members.getContent();

         MessageResponse.Meta meta = MessageResponse.Meta.builder()
               .total(members.getTotalElements())
               .perPage(pageable.getPageSize())
               .currentPage(pageable.getPageNumber())
               .lastPage(members.getTotalPages())
               .build();

         return responseUtil.successWithDataAndMeta(MessageKey.SUCCESS_GET_DATA.getKey(), memberList, meta);

      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // GET MEMBER
   public ResponseEntity<MessageResponse> getMember(Long id) {
      try {
         Optional<Member> memberOpt = memberRepository.findByIdAndDeletedFalse(id);

         if (memberOpt.isEmpty())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), "Member with id " + id.toString());

         return responseUtil.successWithData(MessageKey.SUCCESS_GET_DATA.getKey(), memberOpt.get());
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // UPDATE MEMBER
   @Transactional
   public ResponseEntity<MessageResponse> updateMember(Long id, MemberDto request) {
      try {

         // cek id
         Optional<Member> memberOpt = memberRepository.findById(id);
         if (memberOpt.isEmpty() || memberOpt.get().isDeleted())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), id.toString());

         // cek duplicate email
         if (!memberOpt.get().getId().equals(id)) {
            return responseUtil.badRequest(MessageKey.EMAIL_DUPLICATE.getKey(),null, request.getEmail());
         }

         Member member = memberOpt.get();
         // cek no change
         if (member.getNama().equals(request.getNama()) &&
               member.getAlamat().equals(request.getAlamat()) &&
               member.getEmail().equals(request.getEmail())) {
            return responseUtil.badRequest(MessageKey.ERROR_NO_CHANGE.getKey(), null);
         }

         member.setNama(request.getNama());
         member.setAlamat(request.getAlamat());
         member.setEmail(request.getEmail());
         memberRepository.save(member);
         return responseUtil.successWithData(MessageKey.SUCCESS_UPDATE_DATA.getKey(), member, "Member with name " + member.getNama());
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }

   // DELETE MEMBER
   @Transactional
   public ResponseEntity<MessageResponse> deleteMember(Long id) {
      try {
         Optional<Member> memberOpt = memberRepository.findByIdAndDeletedFalse(id);

         if (memberOpt.isEmpty() || memberOpt.get().isDeleted())
            return responseUtil.notFound(MessageKey.ERROR_NOT_FOUND.getKey(), id.toString());

         Member member = memberOpt.get();
         member.setDeleted(true);
         memberRepository.save(member);

         return responseUtil.success(MessageKey.SUCCESS_DELETE_DATA.getKey(), "Member with id " + id.toString());
      } catch (Exception e) {
         return responseUtil.internalServerError(MessageKey.ERROR_SERVER.getKey());
      }
   }


   
}
