package com.storage.intercepter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.storage.entity.Product;
import com.storage.entity.custom.PageBean;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.ProductRemoteService;
import com.storage.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReminderIntercepter  extends HandlerInterceptorAdapter{



	@Autowired
	
	ProductRemoteService service;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ResponseEntity<String> stockReminder = this.service.getStockReminder();
		String body = stockReminder.getBody();
		StorageResult<PageBean<Product>> pageBeanStorageResult = JsonUtils.jsonToObject(body, new TypeReference<StorageResult<PageBean<Product>>>() {
		});

		request.setAttribute("stockReminder", pageBeanStorageResult.getResult());

		return super.preHandle(request, response, handler);
	}

}
