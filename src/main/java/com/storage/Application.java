package com.storage;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.storage.intercepter.LoginIntercepter;
import com.storage.intercepter.ReminderIntercepter;
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication()
@ServletComponentScan
//@ImportResource("classpath:consumer.xml")
public class Application  implements WebMvcConfigurer{
	@Bean
	public HandlerInterceptor getReminderIntercepter() {

		return new ReminderIntercepter();
	}
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginIntercepter());
		registry.addInterceptor(this.getReminderIntercepter()).excludePathPatterns("/login");
	  registry.addInterceptor(localeChangeInterceptor());
	  
	}


	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/success").setViewName("success");
		
	}


	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);
	}
	
	
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setLocaleAttributeName("local_lang_my");
	    slr.setDefaultLocale(Locale.CHINA);
	    return slr;
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	}



}