package com.storage.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storage.entity.Manager;
import com.storage.remote.service.ManagerRemoteService;
import com.storage.service.ManagerService;

@Controller()
@RequestMapping("/manager")
@PropertySource("classpath:myapp.properties")
public class ManagerController {

	
	@Autowired
	ManagerRemoteService service;

	@Value("${cookie.TokenName}")
	String tokenName;

	@PostMapping("/add")
	public Object addManager(Manager manager) {
		return this.service.addManager(manager);
	}
	/*@PostMapping("/login")
	public ModelAndView loginManager(@AuthenticationPrincipal User user,  ModelAndView model,HttpServletRequest request,HttpServletResponse response, Manager manager) {
		System.out.println("1111111111111111111111");

		return model;
	}*/
	
	@GetMapping("/get/{id}")
	public Object getManager(@PathVariable(name = "id") Integer id) {

		return this.service.getManager(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteManagerById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteManagerById(id);
	}
	/*@GetMapping("/logout")
	public Object signout() {
		return this.service.deleteManagerById(id);
	}*/

	



	@PostMapping("/update")
	public Object updateManager(Manager manager) {
		return this.service.updateManager(manager);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
