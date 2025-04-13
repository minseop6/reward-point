package com.test.rewardpoint.common.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionResponse {

    public String message;

    public static ExceptionResponse from(ServerException serverException) {
        return new ExceptionResponse(serverException.getMessage());
    }

    public static ExceptionResponse createUnknownExceptionResponse() {
        return new ExceptionResponse("알 수 없는 오류가 발생했습니다.");
    }
}
