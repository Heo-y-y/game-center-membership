package com.game.membership.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Member
    EMAIL_ALREADY_EXIST(400, "이미 가입된 이메일입니다."),
    MEMBER_NOT_FOUND(400, "가입된 사용자가 아닙니다."),

    // Game
    GAME_NOT_FOUND(400, "해당 게임이 존재하지 않습니다."),

    // Card
    CARD_NOT_FOUND(400, "해당 카드가 존재하지 않습니다.");

    private final int status;
    private final String message;
}
