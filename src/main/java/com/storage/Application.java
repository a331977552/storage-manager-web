package com.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.storage.intercepter.LoginIntercepter;
import com.storage.intercepter.ReminderIntercepter;

@SpringBootApplication()
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

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		//		registry.addViewController("/home").setViewName("/home");

	}


	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);


	}
	 



}