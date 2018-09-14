package com.storage.controller.advicer;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.storage.entity.custom.StorageResult;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {


    @ExceptionHandler(value 
      = { Exception.class})
    @ResponseBody
    protected StorageResult handleConflict(
      Exception ex, WebRequest request) {
    	String message = ex.getMessage();
    	String bodyOfResponse;
    	if(message.startsWith("The temporary upload location")) {
    		bodyOfResponse = "请联系管理员";  
    	}else {
    		bodyOfResponse=message;
    	}
    	StorageResult<Object> failed = StorageResult.failed(bodyOfResponse);
        return failed;
}
    }