package com.game.membership.domain.slack.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTemplate {
    DOWN("ID{member.id}의 {member.name}님이 {member.level}등급으로 내려갔습나다."),
    UP("ID{member.id}의 {member.name}님이 {member.level}등급으로 올라갔습나다."),
    NEW("ID{member.id}의 {member.name}님이 {member.level}등급으로 가입되었습니다.");

    private String  message;
}
