package com.storage.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class SessionListener implements HttpSessionListener {

	Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void sessionCreated(HttpSessionEvent event) {
    	logger.info("sessionCreated");   
        event.getSession().setMaxInactiveInterval(3600*24);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    	logger.info("session destroyed");
    }
}