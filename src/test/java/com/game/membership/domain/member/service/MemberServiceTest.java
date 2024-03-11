package com.game.membership.domain.member.service;

import com.game.membership.domain.member.dto.MemberFormDto;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.global.error.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void afterEach() {
        memberRepository.deleteAll();
    }

    @Nested
    class SaveMember {
        @Test
        @DisplayName("회원 등록 성공")
        void saveMemberSuccess() throws Exception {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setName("testName");
            dto.setEmail("test@gmail.com");

            // when
            memberService.saveMember(dto);

            // then
            Optional<Member> savedMember = memberRepository.findByEmail(dto.getEmail());
            assertNotNull(savedMember);
            assertEquals(dto.getName(), savedMember.get().getName());
            assertEquals(dto.getEmail(), savedMember.get().getEmail());
            assertEquals(Level.BRONZE, savedMember.get().getLevel());
        }

        @Test
        @DisplayName("가입된 이메일 예외 확인")
        void emailAlreadyExist() throws Exception {
            // given
            MemberFormDto dto1 = new MemberFormDto();
            dto1.setName("testName1");
            dto1.setEmail("test@gmail.com");

            MemberFormDto dto2 = new MemberFormDto();
            dto2.setName("testName2");
            dto2.setEmail("test@gmail.com");

            // when,then
            memberService.saveMember(dto1);
            BusinessException exception = assertThrows(BusinessException.class, () -> memberService.saveMember(dto2));
            assertEquals(exception.getMessage(), "이미 가입된 이메일입니다.");

        }
    }
}