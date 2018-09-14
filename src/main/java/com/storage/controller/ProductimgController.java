package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.storage.entity.Productimg;
import com.storage.remote.service.ProductimgRemoteService;

@RestController()
@RequestMapping("/productimg")
public class ProductimgController {
	
	@Autowired
	ProductimgRemoteService service;

	@PostMapping("/add")
	public Object addProductimg(MultipartFile file) {

		return this.service.addImg(file);
	}
	@PostMapping("/addWithFullURL")
	public Object addWithFullURL(MultipartFile file) {
		
		return this.service.addWithFullURL(file);
	}


	@GetMapping("/get/{id}")
	public Object getProductimg(@PathVariable(name = "id") Integer id) {

		return this.service.getProductimg(id);
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
	}}
