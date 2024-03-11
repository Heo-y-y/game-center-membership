package com.game.membership.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // Member
    MEMBER_SAVE_SUCCESS(200, "회원등록에 성공하였습니다.");

    private final int status;
    private final String message;
}
