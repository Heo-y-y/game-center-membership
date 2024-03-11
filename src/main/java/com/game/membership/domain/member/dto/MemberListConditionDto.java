package com.game.membership.domain.member.dto;

import com.game.membership.domain.member.enumset.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberListConditionDto {
    private String name;
    private Level level;
}
