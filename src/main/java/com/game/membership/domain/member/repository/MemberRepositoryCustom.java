package com.game.membership.domain.member.repository;

import com.game.membership.domain.member.dto.MemberListConditionDto;
import com.game.membership.domain.member.dto.MemberListDto;

import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * 회원 목록 조회
     *
     * @param condition 검색 조건
     * @return 회원 목록 DTO 리스트
     */
    List<MemberListDto> searchAllMembers(MemberListConditionDto condition);
}
