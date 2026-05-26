package com.ec.tecnologia.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class TecUtils {

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<>(
                "{\"message\":\""+responseMessage+"\"}",
                httpStatus
        );
    }

}
