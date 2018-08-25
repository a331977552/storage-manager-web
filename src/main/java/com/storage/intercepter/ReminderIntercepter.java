package com.storage.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.storage.entity.custom.StorageResult;
import com.storage.service.ProductService;

public class ReminderIntercepter  extends HandlerInterceptorAdapter{



	@Autowired
	ProductService service;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		StorageResult stockReminder = this.service.getStockReminder();

		request.setAttribute("stockReminder", stockReminder);

		return super.preHandle(request, response, handler);
	}

}
