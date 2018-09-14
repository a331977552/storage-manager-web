package com.storage.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.storage.entity.Manager;
import com.storage.entity.custom.MyManager;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.ManagerRemoteService;
import com.storage.service.ManagerService;

@Service
public class ManagerDetailService implements UserDetailsService {

	@Autowired
	ManagerRemoteService service;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isEmpty(username.trim())) {
			throw new UsernameNotFoundException("null username");
		}
		Manager manager=new Manager();

		
		manager.setUsername(username);
		StorageResult<List<Manager>> managerByExample = this.service.getManagerByExample(manager);
		
		if(managerByExample.getResult()!=null ) {
			List<Manager> result = managerByExample.getResult();
			if(result.size()==0) {
				throw new UsernameNotFoundException("can find the username: "+ username);
			}
			Manager manager2 = result.get(0);
			List<SimpleGrantedAuthority> authorities=new ArrayList<>();
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");

			authorities.add(simpleGrantedAuthority);
			MyManager manager3=new MyManager(manager2.getUsername(),manager2.getPassword(), authorities);

			manager3.setManager(manager2);
			return manager3;
		}
		throw new UsernameNotFoundException("can find the username: "+ username);
	}

}
