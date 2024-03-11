package com.game.membership.domain.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberFormDtoTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    @DisplayName("유효성 검사 통과")
    void memberFormDto() throws Exception {
        // given
        MemberFormDto dto = new MemberFormDto();
        dto.setName("testName");
        dto.setEmail("test@gmail.com");

        // when
        Errors errors = new BeanPropertyBindingResult(dto, "memberFormDto");
        validator.validate(dto, errors);

        // then
        assertFalse(errors.hasErrors());
    }

    @Test
    @DisplayName("이름 입력값 길이 확인")
    void checkNameCount() throws Exception {
        // given
        MemberFormDto dto = new MemberFormDto();
        dto.setName("a");

        // when
        Errors errors = new BeanPropertyBindingResult(dto, "memberFormDto");
        validator.validate(dto, errors);

        // then
        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("이름은 최소 2자리 이상 100문자 이하여야 합니다.", errors.getFieldError("name").getDefaultMessage());
    }

    @Test
    @DisplayName("이름 공백 확인")
    void checkNameNotBlank() throws Exception {
        // given
        MemberFormDto dto = new MemberFormDto();
        dto.setName(null);

        // when
        Errors errors = new BeanPropertyBindingResult(dto, "memberFormDto");
        validator.validate(dto, errors);

        // then
        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("이름을 입력해주세요.", errors.getFieldError("name").getDefaultMessage());
    }

    @Test
    @DisplayName("이메일 형식 확인")
    void checkEmailFormat() throws Exception {
        // given
        MemberFormDto dto = new MemberFormDto();
        dto.setEmail("testEmail");

        // when
        Errors errors = new BeanPropertyBindingResult(dto, "memberFormDto");
        validator.validate(dto, errors);

        // then
        assertTrue(errors.hasFieldErrors("email"));
        assertEquals("이메일 형식이 올바르지 않습니다.", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    @DisplayName("이메일 공백 확인")
    void checkEmailNotBlank() throws Exception {
        // given
        MemberFormDto dto = new MemberFormDto();
        dto.setEmail(null);

        // when
        Errors errors = new BeanPropertyBindingResult(dto, "memberFormDto");
        validator.validate(dto, errors);

        // then
        assertTrue(errors.hasFieldErrors("email"));
        assertEquals("이메일을 입력해주세요.", errors.getFieldError("email").getDefaultMessage());

    }
}