package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.StOrder;
import com.storage.service.StOrderService;

@RestController()
@RequestMapping("/stOrder")
public class StOrderController {

	@Autowired
	StOrderService service;

	@PostMapping("/add")
	public Object addStOrder(StOrder stOrder) {

		return this.service.addStOrder(stOrder);
	}

	@GetMapping("/get/{id}")
	public Object getStOrder(@PathVariable(name = "id") Integer id) {

		return this.service.getStOrderById(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteStOrderById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteStOrderById(id);
	}



	@PostMapping("/update")
	public Object updateStOrder(StOrder stOrder) {
		return this.service.updateStOrder(stOrder);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
