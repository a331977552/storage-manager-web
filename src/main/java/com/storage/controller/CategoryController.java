package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Category;
import com.storage.entity.custom.CustomeCategory;
import com.storage.service.CategoryService;

@RestController()
@RequestMapping("/category")
@PropertySource("classpath:myapp.properties")
public class CategoryController {


			
	@Autowired
	CategoryService service;

	@PostMapping("/add")
	public Object addCategory(Category category) {

		return this.service.addCategory(category);
	}

	@GetMapping("/get/{id}")
	public Object getCategory(@PathVariable(name = "id") Integer id) {

		return this.service.getCategoryById(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteCategoryById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteCategoryById(id);
	}



	/*	@PostMapping("/update")
	public Object updateCategory(Category category) {
		return this.service.updateCategory(category);
	}*/
	@PostMapping("/update")
	public Object updateCategory(CustomeCategory category) {
		Category category2=new Category();
		category2.setId((int)category.getPk());
		category2.setName(category.getValue());
		return this.service.updateCategory(category2);
	}

	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
