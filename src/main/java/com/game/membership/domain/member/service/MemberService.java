package com.game.membership.domain.member.service;

import com.game.membership.domain.member.dto.MemberFormDto;
import com.game.membership.domain.member.entity.Member;
import com.game.membership.domain.member.enumset.Level;
import com.game.membership.domain.member.repository.MemberRepository;
import com.game.membership.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.game.membership.global.error.ErrorCode.EMAIL_ALREADY_EXIST;
import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveMember(MemberFormDto dto) {
        if (!hasText(dto.getName()))
            throw new IllegalArgumentException("이름을 입력해주세요.");
        if (dto.getName().length() < 2 || dto.getName().length() >= 100) {
            throw new IllegalArgumentException("이름은 최소 2자리 이상 100문자 이하여야 합니다.");
        }
        if (!hasText(dto.getEmail())) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        String emailReg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if (!Pattern.matches(emailReg, dto.getEmail())) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        Optional<Member> existMember = memberRepository.findByEmail(dto.getEmail());

        if (existMember.isPresent()) throw new BusinessException(EMAIL_ALREADY_EXIST);

        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .level(Level.BRONZE)
                .build();

        memberRepository.save(member);
    }
}
