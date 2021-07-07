package com.semmet.personattribute.personattribute.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InvalidBodyExceptionHandler is a custom exception thrown when an invalid body content
 * is posted in a POST request
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid body")
public class InvalidBodyExceptionHandler extends RuntimeException {
    
}
