package com.storage.intercepter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.netflix.discovery.converters.Auto;
import com.storage.entity.Product;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.ProductRemoteService;
import com.storage.service.ProductService;

public class ReminderIntercepter  extends HandlerInterceptorAdapter{



	@Autowired
	
	ProductRemoteService service;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		StorageResult<List<Product>> stockReminder = this.service.getStockReminder();
		
		

		request.setAttribute("stockReminder", stockReminder);

		return super.preHandle(request, response, handler);
	}

}
