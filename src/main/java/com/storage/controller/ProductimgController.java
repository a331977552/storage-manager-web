package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Productimg;
import com.storage.service.ProductimgService;

@RestController()
@RequestMapping("/productimg")
public class ProductimgController {

	@Autowired
	ProductimgService service;

	@PostMapping("/add")
	public Object addProductimg(Productimg productimg) {

		return this.service.addProductimg(productimg);
	}

	@GetMapping("/get/{id}")
	public Object getProductimg(@PathVariable(name = "id") Integer id) {

		return this.service.getProductimgById(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteProductimgById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteProductimgById(id);
	}



	@PostMapping("/update")
	public Object updateProductimg(Productimg productimg) {
		return this.service.updateProductimg(productimg);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
