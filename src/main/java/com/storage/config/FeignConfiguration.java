package com.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import feign.Request;

@Configuration
public class FeignConfiguration {

	   /*@Bean
	    public Decoder feignDecoder() {
		   HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
	        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
	        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
	    }
	   @Bean
	   public Encoder feignEncoder() {
		   HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
		   ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);		   
		   return new SpringEncoder(objectFactory);
	   }
	    public ObjectMapper customObjectMapper(){
	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper;
	    }*/
	/*
	ConfigurationManager.getConfigInstance()
    .setProperty("hystrix.command.PhotoService#getToken(String,String,String).execution.isolation.thread.timeoutInMilliseconds", 7000);*/
	/*@Bean
	public ErrorDecoder getErrorDecoder() {
		
		return new FeignClientExceptionHandling();
		
	}*/
	 
	 @Bean
	    public static Request.Options requestOptions(ConfigurableEnvironment env) {
	        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 70000);
	        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 60000);

	        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
	    }

	
}
