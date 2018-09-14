package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Orderitem;
import com.storage.remote.service.OrderitemRemoteService;
import com.storage.service.OrderitemService;

@RestController()
@RequestMapping("/orderitem")
public class OrderitemController {
	
	@Autowired
	OrderitemRemoteService service;

	@PostMapping("/add")
	public Object addOrderitem(Orderitem orderitem) {

		return this.service.addOrderitem(orderitem);
	}

	@GetMapping("/get/{id}")
	public Object getOrderitem(@PathVariable(name = "id") Integer id) {

		return this.service.getOrderitem(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteOrderitemById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteOrderitemById(id);
	}



	@PostMapping("/update")
	public Object updateOrderitem(Orderitem orderitem) {
		return this.service.updateOrderitem(orderitem);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
