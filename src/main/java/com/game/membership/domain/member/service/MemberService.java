package com.game.membership.domain.member.service;

import com.game.membership.domain.card.entity.Card;
import com.game.membership.domain.card.repository.CardRepository;
import com.game.membership.domain.card.service.CardService;
import com.game.membership.domain.member.dto.*;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.domain.slack.enumset.MessageTemplate;
import com.game.membership.domain.slack.service.SlackService;
import com.game.membership.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.game.membership.global.error.ErrorCode.EMAIL_ALREADY_EXIST;
import static com.game.membership.global.error.ErrorCode.MEMBER_NOT_FOUND;
import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CardService cardService;
    private final SlackService slackService;

    @Transactional
    public void saveMember(MemberFormDto dto) {

        memberFormValidation(dto);

        Optional<Member> existMember = memberRepository.findByEmail(dto.getEmail());

        if (existMember.isPresent()) throw new BusinessException(EMAIL_ALREADY_EXIST);

        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .createdAt(dto.getDate())
                .level(Level.BRONZE)
                .build();
        Member saveMember = memberRepository.save(member);

        slackService.sendMessage(slackService.createSlackMessage(saveMember, MessageTemplate.NEW));
    }

    @Transactional
    public void updateMember(MemberFormDto dto, Long id) {

        memberFormValidation(dto);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        if (!member.getEmail().equals(dto.getEmail()) && memberRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException(EMAIL_ALREADY_EXIST);
        }

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setUpdatedAt(dto.getDate());
    }

    public MemberDto getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        return convertToMemberDto(member);
    }

    public List<MemberListDto> searchAllMembers(MemberListConditionDto condition) {
        return memberRepository.searchAllMembers(condition);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        List<Card> cards = cardRepository.findAllByMember(member);

        cardRepository.deleteAll(cards);
        memberRepository.delete(member);
    }

    private MemberDto convertToMemberDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        return dto;
    }

    private static void memberFormValidation(MemberFormDto dto) {
        if (!hasText(dto.getName()))
            throw new IllegalArgumentException("이름을 입력해주세요.");
        if (dto.getName().length() < 2 || dto.getName().length() >= 100) {
            throw new IllegalArgumentException("이름은 2자 이상 100글자 이하여야합니다.");
        }
        if (!hasText(dto.getEmail())) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        String emailReg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if (!Pattern.matches(emailReg, dto.getEmail())) {
            throw new IllegalArgumentException("이메일형식이 올바르지 않습니다.");
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate allowedDate = currentDate.minus(1, ChronoUnit.YEARS);

        if (dto.getDate() == null) {
            throw new IllegalArgumentException("날짜를 선택해주세요.");
        }
        if (dto.getDate().isBefore(allowedDate) ||  dto.getDate().isAfter(currentDate)) {
            throw new IllegalArgumentException("가입일자는 현재 날짜로부터 1년 이내여야 합니다.");
        }
    }
}
