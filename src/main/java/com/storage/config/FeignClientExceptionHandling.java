package com.storage.config;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignClientExceptionHandling implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
//		
//		HttpServletRequest
//		HttpServletResponse
//		ContextConfig
		Default default1 = new ErrorDecoder.Default();
		return default1.decode(methodKey, response);
	}


	 /*   @Override
	    public Exception decode(String methodKey, Response response) {
	        if (response.status() >= 400 && response.status() <= 499) {
	          
	        }
	        if (response.status() >= 500 && response.status() <= 599) {
	            
	        }
	       
	    }*/

	

}
