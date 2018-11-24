package com.storage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Category;
import com.storage.entity.custom.CustomeCategory;
import com.storage.remote.service.CategoryRemoteService;

@RestController()
@RequestMapping("/category")
@PropertySource("classpath:myapp.properties")
public class CategoryController  {


	
	@Autowired
	CategoryRemoteService service;
	Logger logger = LoggerFactory.getLogger(CategoryController.class);
	@PostMapping("/add")
	public Object addCategory(Category category) {
		logger.error(category.toString());

		return this.service.addCategory(category);
	}

	@GetMapping("/get/{id}")
	public Object getCategory(@PathVariable(name = "id") Integer id) {

		return this.service.getCategory(id);
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
		category2.setText(category.getValue());
		return this.service.updateCategory(category2);
	}
	@PostMapping("/updateAll")
	public Object updateAllCategories(@RequestBody List<Category> categories) {
		return this.service.updateAllCategory(categories).getBody();
	}

	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}
	@GetMapping("/findAll")
	public Object findAll() {
		ResponseEntity<String> findAll = this.service.findAll();
		return findAll.getBody();
	}

}
