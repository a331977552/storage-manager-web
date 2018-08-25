package com.storage.entity.custom;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.storage.entity.Manager;

public class MyManager extends User implements Serializable{
	private Manager manager;

	public Manager getManager() {
		return this.manager;
	}


	public void setManager(Manager manager) {
		this.manager = manager;
	}


	public MyManager(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}


	public MyManager(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
}