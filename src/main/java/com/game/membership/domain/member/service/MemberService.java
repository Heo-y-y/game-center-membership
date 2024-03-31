package com.game.membership.domain.member.service;

import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.card.repository.CardRepository;
import com.game.membership.domain.member.dto.MemberDto;
import com.game.membership.domain.member.dto.MemberListConditionDto;
import com.game.membership.domain.member.dto.MemberListDto;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.domain.slack.enumset.MessageTemplate;
import com.game.membership.domain.slack.service.SlackService;
import com.game.membership.global.error.BusinessException;
import com.game.membership.web.member.form.MemberSaveForm;
import com.game.membership.web.member.form.MemberUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.game.membership.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final SlackService slackService;

    /**
     * 회원 등록
     *
     * @param form
     */
    @Transactional
    public void saveMember(MemberSaveForm form) {

        Member member = Member.builder()
                .name(form.getName())
                .email(form.getEmail())
                .createdAt(form.getCreatedAt())
                .level(Level.BRONZE)
                .build();
        Member saveMember = memberRepository.save(member);

        slackService.sendMessage(slackService.createSlackMessage(saveMember, MessageTemplate.NEW));
    }

    /**
     * 회원 수정
     *
     * @param form
     * @param id
     */
    @Transactional
    public void updateMember(MemberUpdateForm form, Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setUpdatedAt(form.getUpdatedAt());
    }


    /**
     * 회원 정보 조회
     */
    public MemberDto getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return convertToMemberDto(member);
    }

    /**
     * 회원 검색
     *
     * @param condition
     * @return memberRepository.searchAllMembers(condition)
     */
    public List<MemberListDto> searchAllMembers(MemberListConditionDto condition) {
        return memberRepository.searchAllMembers(condition);
    }

    /**
     * 회원 삭제
     *
     * @param id
     */
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        List<Card> cards = cardRepository.findAllByMember(member);

        cardRepository.deleteAll(cards);
        memberRepository.delete(member);
    }

    /**
     * 회원 등록 폼 복합 유효성 검증
     *
     * @param createdAt
     * @param email
     * @param bindingResult
     */
    public void checkSaveFormValid(LocalDate createdAt, String email, BindingResult bindingResult) {

        if (createdAt == null) {
            bindingResult.rejectValue("createdAt", "error.member", "날짜를 선택하세요.");
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalDate allowedDate = currentDate.minus(1, ChronoUnit.YEARS);

            if (createdAt.isBefore(allowedDate) ||  createdAt.isAfter(currentDate)) {
                bindingResult.rejectValue("createdAt", "error.member", "가입일자는 현재 날짜로부터 1년 이내여야 합니다.");
            }
        }

        Optional<Member> existMember = memberRepository.findByEmail(email);
        if (existMember.isPresent()) {
            bindingResult.rejectValue("email", "error.member", "이미 가입된 이메일입니다.");
        }
    }

    /**
     * 회원 수정 폼 복합 유효성 검증
     *
     * @param updatedAt
     * @param email
     * @param id
     * @param bindingResult
     */
    public void checkUpdateFormValid(LocalDate updatedAt, String email, Long id, BindingResult bindingResult) {

        if (updatedAt == null) {
            bindingResult.rejectValue("updatedAt", "error.member", "날짜를 선택하세요.");
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalDate allowedDate = currentDate.minus(1, ChronoUnit.YEARS);

            if (updatedAt.isBefore(allowedDate) ||  updatedAt.isAfter(currentDate)) {
                bindingResult.rejectValue("updatedAt", "error.member", "수정일자는 현재 날짜로부터 1년 이내여야 합니다.");
            }
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        if (!member.getEmail().equals(email) && memberRepository.existsByEmail(email)) {
            bindingResult.rejectValue("email", "error.member", "이미 가입된 이메일입니다.");
        }
    }

    public Member findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        return member;
    }

    private MemberDto convertToMemberDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        return dto;
    }
}
