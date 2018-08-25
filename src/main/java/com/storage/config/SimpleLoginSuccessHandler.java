package com.storage.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.storage.entity.custom.MyManager;

public class SimpleLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession();
		MyManager manager=(MyManager) authentication.getPrincipal();
		session.setAttribute("user",manager);
		session.setMaxInactiveInterval(3600);
		response.sendRedirect(request.getContextPath()+"/index");

		//		TokenBasedRememberMeServices
		/*if( manager.isRemember()) {
			Manager r=(Manager) result.getResult();
			CookieUtils.setCookie(request, response, this.tokenName,r.getToken() );
		}*/


	}



}
