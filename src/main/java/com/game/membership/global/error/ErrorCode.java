package com.game.membership.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Member
    EMAIL_ALREADY_EXIST(400, "이미 가입된 이메일입니다.");

    private final int status;
    private final String message;
}
