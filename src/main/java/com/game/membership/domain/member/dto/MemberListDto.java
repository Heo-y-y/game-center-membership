package com.game.membership.domain.member.dto;

import com.game.membership.domain.member.enumset.Level;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberListDto {

    private Long id;
    private String name;

    private String email;

    private LocalDate createdAt;

    private Level level;

    private Long cardCount;
}
