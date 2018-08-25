package com.storage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.storage.entity.Category;
import com.storage.entity.Customer;
import com.storage.entity.Vat;
import com.storage.entity.custom.CustomOrder;
import com.storage.entity.custom.MyManager;
import com.storage.entity.custom.OrderWrap;
import com.storage.entity.custom.StorageResult;
import com.storage.service.CategoryService;
import com.storage.service.CustomerService;
import com.storage.service.ProductService;
import com.storage.service.SettingService;
import com.storage.service.StOrderService;
import com.storage.service.VatService;
import com.storage.utils.JsonUtils;

@Controller
public class HomeController {

	@Autowired
	SettingService service;
	@Autowired
	CustomerService customerService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	StOrderService orderService;
	@Autowired
	VatService vatService;

	
	@RequestMapping({"/","/index","index.html"})
	public ModelAndView index(HttpServletRequest request,ModelAndView model){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(principal==null) {
			model.setViewName("login");
		}else {
			if(principal instanceof MyManager) {
				MyManager manager=(MyManager) principal;
				model.addObject("user",manager);
			}

			model.setViewName("index");
		}


		return model;
	}
	

	@RequestMapping({"/addproduct"})
	public ModelAndView addproduct(ModelAndView model){
		Category category=new Category();
		StorageResult categoryByExample = this.categoryService.getCategoryByExample(category);
		Vat vat=new Vat();
		StorageResult vatByExample = vatService.getVatByExample(vat);
		model.setViewName("addproduct");
		model.addObject("categories",categoryByExample.getResult());
		model.addObject("vat",vatByExample.getResult());

		return model;
	}

	@RequestMapping({"/searchproduct"})
	public ModelAndView searchproduct(ModelAndView model){
		Category category=new Category();
		StorageResult categoryByExample = this.categoryService.getCategoryByExample(category);

		model.addObject("categories",categoryByExample);


		model.setViewName("searchproduct");
		return model;
	}
	@GetMapping("/categorylist")

	public ModelAndView listCategory(ModelAndView model) {
		Category category=new Category();
		StorageResult categoryByExample = this.categoryService.getCategoryByExample(category);
		if(categoryByExample.getCode()!=200) {
			model.setViewName("error");
			model.addObject("msg",categoryByExample.getMsg());
		}else {
			model.setViewName("edgecategory");
			model.addObject("categories",categoryByExample.getResult());
		}

		return model;
	}

	@RequestMapping({"/order"})
	public ModelAndView order(ModelAndView model){
		Category category=new Category();
		StorageResult categoryByExample = this.categoryService.getCategoryByExample(category);
		model.addObject("categories",categoryByExample);
		model.setViewName("order");
		return model;
	}
	@RequestMapping({"/order_review"})
	public ModelAndView order_review(ModelAndView model,String order,Customer customer){
		List<CustomOrder> jsonToList = JsonUtils.jsonToList(order, CustomOrder.class);

		if(customer.getId()==null || customer.getId()<=0) {
			customer.setId(null);
			StorageResult addCustomer = customerService.addCustomer(customer);
			if(addCustomer.isSuccess()) {
				customer=(Customer) addCustomer.getResult();
			}else {
				//theoretically it is impossible to go here as there are checking in front-end
				model.setViewName("order");
				return model;
			}
		}
		StorageResult productByExample = this.productService.getProductByExample(jsonToList);

		orderService.creaOrder(customer,(OrderWrap)productByExample.getResult());
		if(productByExample.isSuccess()) {
			model.addObject("order",productByExample.getResult());
			model.addObject("customer",customer);
			model.setViewName("order_review");
		}else {
			model.addObject("code",productByExample.getCode());
			model.addObject("msg",productByExample.getMsg());
			model.setViewName("error");
		}
		return model;
	}


	@RequestMapping("/setting")
	public ModelAndView setting(ModelAndView model){
		StorageResult settingById = this.service.getSettingById(0);
		if(settingById.getResult()!=null) {
			model.addObject("setting",settingById.getResult());
		}
		model.setViewName("setting");
		return model;
	}
	@RequestMapping("/statement")
	public ModelAndView statement(ModelAndView model){

		model.setViewName("statement");
		return model;
	}




}
