package com.storage.config;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.storage.entity.custom.StorageResult;

@ControllerAdvice()
public class MyExceptionHandler {

	@ExceptionHandler(SizeLimitExceededException.class)
	@ResponseBody
	public StorageResult handleException(SizeLimitExceededException e) {
		System.out.println("handleException");
		return StorageResult.failed("file is too large, size cannot exceed 5MB");
	}


}
