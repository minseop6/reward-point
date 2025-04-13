package com.test.rewardpoint.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
@RequiredArgsConstructor
public class BadRequestException extends ServerException {

    protected final String message;
}
