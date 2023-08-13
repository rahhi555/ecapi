package com.example.ecapi.controller.advice;

import com.example.ecapi.model.ErrorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorBase> handleRuntimeException(RuntimeException e) {
        Logger logger = LoggerFactory.getLogger(e.getCause().getClass());
        logger.error(e.getMessage(), e);
        var err = new ErrorBase("Internal Server Error", "不明なエラーです");
        return ResponseEntity.internalServerError().body(err);
    }

}
