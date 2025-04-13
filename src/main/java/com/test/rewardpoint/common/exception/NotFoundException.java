package com.test.rewardpoint.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
@RequiredArgsConstructor
public class NotFoundException extends ServerException {

    protected final String message;
}
