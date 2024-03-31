package com.game.membership.domain.member.dto;

import com.game.membership.domain.member.enumset.Level;
import lombok.Data;

@Data
public class MemberListConditionDto {
    private String name;

    private Level level;
}
