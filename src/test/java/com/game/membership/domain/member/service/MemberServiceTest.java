package com.game.membership.domain.member.service;

import com.game.membership.domain.member.dto.MemberDto;
import com.game.membership.domain.member.dto.MemberFormDto;
import com.game.membership.domain.member.dto.MemberListConditionDto;
import com.game.membership.domain.member.dto.MemberListDto;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private final MemberListConditionDto condition = new MemberListConditionDto();

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

    @Nested
    class UpdateMember {
        @Test
        @DisplayName("회원 수정 성공")
        void updateMemberSuccess() throws Exception {
            // given
            Member member = new Member();
            member.setName("testName");
            member.setEmail("test@gmail.com");
            Member saveMember = memberRepository.save(member);

            MemberFormDto dto = new MemberFormDto();
            dto.setName("changeName");
            dto.setEmail("change@gmail.com");

            // when
            memberService.updateMember(dto, saveMember.getId());
            Member updatedMember = memberRepository.findById(saveMember.getId()).orElse(null);

            // then
            assertNotNull(updatedMember);
            assertEquals("changeName", updatedMember.getName());
            assertEquals("change@gmail.com", updatedMember.getEmail());
        }

        @Test
        @DisplayName("회원 찾기 실패")
        void memberNotFound() throws Exception {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setName("testName");
            dto.setEmail("test@gmail.com");

            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> memberService.updateMember(dto, 1L));
            assertEquals(exception.getMessage(), "가입된 사용자가 아닙니다.");
        }

        @Test
        @DisplayName("존재하는 이메일 확인")
        void emailAlreadyExist() throws Exception {
            // given
            Member testMember = new Member();
            testMember.setName("testName");
            testMember.setEmail("test@gmail.com");
            Member saveMember = memberRepository.save(testMember);

            Member anotherMember = new Member();
            anotherMember.setName("anotherName");
            anotherMember.setEmail("test@naver.com");
            memberRepository.save(anotherMember);

            MemberFormDto dto = new MemberFormDto();
            dto.setName("changeName");
            dto.setEmail("test@naver.com");

            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> memberService.updateMember(dto, saveMember.getId()));
            assertEquals(exception.getMessage(), "이미 가입된 이메일입니다.");

        }
    }

    @Nested
    class SearchAllMembers {

        @Test
        @DisplayName("이름 검색 성공")
        void searchAllMemberNameSuccess() throws Exception {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setName("박효신");
            dto.setEmail("hyo@gmail.com");

            MemberFormDto dto1 = new MemberFormDto();
            dto1.setName("박지성");
            dto1.setEmail("mu@gmail.com");

            memberService.saveMember(dto);
            memberService.saveMember(dto1);

            condition.setName("박지성");
            // when
            List<MemberListDto> list = memberService.searchAllMembers(condition);

            // then
            list.forEach(member -> {
                assertThat(member.getName()).isEqualTo("박지성");
                assertThat(member.getEmail()).isEqualTo("mu@gmail.com");
            });
        }

        @Test
        @DisplayName("레벨 검색 성공")
        void searchAllMemberLevelSuccess() throws Exception {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setName("박효신");
            dto.setEmail("hyo@gmail.com");

            MemberFormDto dto1 = new MemberFormDto();
            dto1.setName("박지성");
            dto1.setEmail("mu@gmail.com");

            memberService.saveMember(dto);
            memberService.saveMember(dto1);

            condition.setLevel(Level.BRONZE);
            // when
            List<MemberListDto> list = memberService.searchAllMembers(condition);

            // then
            list.forEach(member -> {
                assertThat(member.getLevel()).isEqualTo(Level.BRONZE);
            });
        }
    }

    @Nested
    class MemberDelete {

        @Test
        @DisplayName("회원정보 삭제 성공")
        void deleteMemberSuccess() throws Exception {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setName("testName");
            dto.setEmail("test@gmail.com");

            // when
            memberService.saveMember(dto);
            Optional<Member> savedMember = memberRepository.findByEmail(dto.getEmail());
            memberService.deleteMember(savedMember.get().getId());
            Optional<Member> deletedMember = memberRepository.findById(savedMember.get().getId());

            // then
            assertThat(deletedMember).isEmpty();

        }

        @Test
        @DisplayName("존재하는 유저 없음")
        void memberDeleteMemberNotFound() throws Exception {

            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> memberService.deleteMember(1L));
            assertEquals(exception.getMessage(), "가입된 사용자가 아닙니다.");
        }
    }

    @Nested
    class GetMember {
        @Test
        @DisplayName("회원상세 조회 성공")
        void getMemberSuccess() throws Exception {
            // given
            MemberFormDto dto1 = new MemberFormDto();
            dto1.setName("testName");
            dto1.setEmail("test@gmail.com");
            memberService.saveMember(dto1);
            Optional<Member> saveMember = memberRepository.findByEmail(dto1.getEmail());

            // when
            MemberDto member = memberService.getMember(saveMember.get().getId());

            // then
            assertNotNull(member);
            assertEquals("testName", member.getName());
            assertEquals("test@gmail.com", member.getEmail());
        }

        @Test
        @DisplayName("MemberNotFound")
        void getMemberMemberNotFound() throws Exception {
            // when, then
            BusinessException exception = assertThrows(BusinessException.class, () -> memberService.getMember(1L));
            assertEquals(exception.getMessage(), "가입된 사용자가 아닙니다.");
        }
    }

    @Nested
    class MemberFormValidation {

        @Test
        @DisplayName("이름 공백")
        void nameBlank() {

            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setEmail("test@gmail.com");
            dto.setName("");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> memberService.saveMember(dto));
            assertEquals("이름을 입력해주세요.", exception.getMessage());
        }

        @Test
        @DisplayName("이메일 공백")
        void emailBlank() {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setEmail("");
            dto.setName("test");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> memberService.saveMember(dto));
            assertEquals("이메일을 입력해주세요.", exception.getMessage());
        }

        @Test
        @DisplayName("이름 제한 수")
        void nameCount() {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setEmail("test@gmail.com");
            dto.setName("t");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> memberService.saveMember(dto));
            assertEquals("이름은 2자 이상 100글자 이하여야합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("이메일 형식")
        void emailForm() {
            // given
            MemberFormDto dto = new MemberFormDto();
            dto.setEmail("testgma");
            dto.setName("test");

            // when, then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> memberService.saveMember(dto));
            assertEquals("이메일형식이 올바르지 않습니다.", exception.getMessage());
        }

    }
}