package com.weilango.web.verification;

import com.weilango.web.verification.ReceptionRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExHandler {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handValidException(MethodArgumentNotValidException e){
        return ReceptionRes.builder()
                .status(500)
                .message(e.getBindingResult().getFieldError().getDefaultMessage())
                .build();
    }

}
