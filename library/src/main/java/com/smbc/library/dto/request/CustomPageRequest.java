package com.smbc.library.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomPageRequest {
   private int page = 1;
   private int size = 10;
   private String sort = "nama,asc";

   public Pageable getPage(String defaultSort) {

      String sorting = (sort != null && !sort.isEmpty()) ? sort : defaultSort;
      String[] sortParams = sorting.split(",");

      String sortField = sortParams[0];
      Sort.Direction sortDirection = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

            int currentPage = (page <= 1) ? 0 : page - 1;

      return PageRequest.of(currentPage, size, Sort.by(sortDirection, sortField));
   }
}
