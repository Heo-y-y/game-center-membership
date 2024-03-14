package com.game.membership.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // Member
    MEMBER_SAVE_SUCCESS(200, "회원등록에 성공하였습니다."),
    MEMBER_UPDATE_SUCCESS(200, "회원수정에 성공하였습니다."),
    MEMBER_DELETE_SUCCESS(200, "회원삭제에 성공하였습니다"),

    // Game

    // Card
    CARD_SAVE_SUCCESS(200, "카드등록에 성공하였습니다."),
    CARD_DELETE_SUCCESS(200, "카드삭제에 성공하였습니다.");

    private final int status;
    private final String message;
}
