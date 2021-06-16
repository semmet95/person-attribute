package com.semmet.personattribute.personattribute.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid body")
public class InvalidBodyExceptionHandler extends RuntimeException {
    
}
