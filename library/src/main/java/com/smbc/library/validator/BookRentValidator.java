package com.smbc.library.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.smbc.library.dto.BookRentDto;
import com.smbc.library.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookRentValidator implements Validator {

    private final ValidationUtil validationUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return BookRentDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookRentDto bookRentDto = (BookRentDto) target;

        validationUtil.validateIsbnList(bookRentDto.getIsbn(), "isbn", "List ISBN", errors);
        validationUtil.validateLongId(bookRentDto.getMemberId(), "memberId", "Member ID", errors);
        validationUtil.validateDueDate(bookRentDto.getDueDate(), "dueDate", "Due Date", errors);
    }
}
