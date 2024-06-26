package com.game.membership.global.response;

import lombok.Getter;

@Getter
public class ResultResponse {

    private int status;

    private String message;

    private Object data;

    public ResultResponse(ResultCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResultResponse(int resultCode, String message) {
        this.status = resultCode;
        this.message = message;
    }

    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public static ResultResponse of(ResultCode resultCode) {
        return new ResultResponse(resultCode, null);
    }
}
