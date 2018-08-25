package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Customer;
import com.storage.service.CustomerService;

@RestController()
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerService service;

	@PostMapping("/add")
	public Object addCustomer(Customer customer) {

		return this.service.addCustomer(customer);
	}

	@GetMapping("/get/{id}")
	public Object getCustomer(@PathVariable(name = "id") Integer id) {
		return this.service.getCustomerById(id);
	}
	@GetMapping("/getbyName")
	@ResponseBody
	public Object getCustomerByName(@RequestParam() String name) {
		return this.service.getCustomerByName(name);
	}

	@GetMapping("/getName/{order}.json")
	@ResponseBody
	public Object getCustomer(@PathVariable(name="order")String order) {

		return this.service.getCustomerList(order);
	}

	@GetMapping("/list")
	@ResponseBody
	public Object list() {

		return this.service.List();
	}

	@GetMapping("/delete/{id}")
	public Object deleteCustomerById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteCustomerById(id);
	}



	@PostMapping("/update")
	public Object updateCustomer(Customer customer) {
		return this.service.updateCustomer(customer);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
