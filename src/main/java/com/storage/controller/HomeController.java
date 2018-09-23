package com.storage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.storage.entity.Advertisement;
import com.storage.entity.Carousel;
import com.storage.entity.Category;
import com.storage.entity.Customer;
import com.storage.entity.Vat;
import com.storage.entity.custom.CustomProduct;
import com.storage.entity.custom.MyManager;
import com.storage.entity.custom.OrderWrap;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.AdvertisementServiceRemote;
import com.storage.remote.service.CarouselRemoteService;
import com.storage.remote.service.CategoryRemoteService;
import com.storage.remote.service.CustomerRemoteService;
import com.storage.remote.service.ProductRemoteService;
import com.storage.remote.service.SettingRemoteService;
import com.storage.remote.service.StOrderRemoteService;
import com.storage.remote.service.VatRemoteService;
import com.storage.utils.JsonUtils;

@Controller
public class HomeController {
	
	@Autowired
	SettingRemoteService settingService;
	
	@Autowired
	CustomerRemoteService customerService;
	
	@Autowired
	CategoryRemoteService categoryService;
	
	@Autowired
	ProductRemoteService productService;
	
	@Autowired
	StOrderRemoteService orderService;
	
	@Autowired
	VatRemoteService vatService;

	@Autowired
	CarouselRemoteService carouselService;

	@Autowired
	AdvertisementServiceRemote adService;
	
	

	

	@RequestMapping({"/addproduct"})
	public ModelAndView addproduct(ModelAndView model){
	
		ResponseEntity<String> categoryByExample = this.categoryService.findAll();
		StorageResult<List<Category>> jsonToObject = JsonUtils.jsonToObject(categoryByExample.getBody(), new TypeReference<StorageResult<List<Category>>>() {
		} );
		ResponseEntity<String> vatByExample =vatService.findAll();
		StorageResult<List<Vat>> vats = JsonUtils.jsonToObject(vatByExample.getBody(), new TypeReference<	StorageResult<List<Vat>>>() {
		} );
		model.setViewName("addproduct");
		model.addObject("categories",jsonToObject.getResult());
		model.addObject("vat",vats.getResult());
		return model;
	}

	@RequestMapping("/findAllCarousel")
	public ModelAndView findAllCarousel(ModelAndView model){
		StorageResult<List<Carousel>> allCarousel = carouselService.getAllCarousel();
		model.setViewName("advertise_edit");
		
		List<Advertisement> findAdvertisementByPosition = adService.findAdvertisementByPosition(1);
		if(!findAdvertisementByPosition.isEmpty()) {
			model.addObject("topAd", allCarousel.getResult().get(0));	
		}else {
			Advertisement ad=new Advertisement();
			ad.setMessage("");
			ad.setPosition(1);
			Advertisement addAdvertisement = adService.addAdvertisement(ad);
			model.addObject("topAd", addAdvertisement);	
		}
	
		List<Advertisement> findAdvertisementByPosition2 = adService.findAdvertisementByPosition(2);
		if(!findAdvertisementByPosition2.isEmpty()) {
			model.addObject("leftAd", allCarousel.getResult().get(0));
			
		}else {
			Advertisement ad=new Advertisement();
			ad.setClickUrl("");
			ad.setImgUrl("");
			ad.setPosition(2);
			Advertisement addAdvertisement = adService.addAdvertisement(ad);
			model.addObject("leftAd", addAdvertisement);	
		}
		
		model.addObject("carousels", allCarousel.getResult());
		return model;
	}


	@RequestMapping({"/searchproduct","/","/index","index.html"})
	public ModelAndView searchproduct(ModelAndView model){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(principal==null) {
			model.setViewName("login");
		}else {
			if(principal instanceof MyManager) {
				MyManager manager=(MyManager) principal;
				model.addObject("user",manager);
			}
		}
		
		
		ResponseEntity<String> categoryByExample = this.categoryService.findAll();
		StorageResult<List<Category>> jsonToObject = JsonUtils.jsonToObject(categoryByExample.getBody(), new TypeReference<StorageResult<List<Category>>>() {
		} );
		model.addObject("categories",jsonToObject);


		model.setViewName("searchproduct");
		return model;
	}
	@GetMapping("/categorylist")

	public ModelAndView listCategory(ModelAndView model) {
		ResponseEntity<String> categoryByExample = this.categoryService.findAll();
		StorageResult<List<Category>> jsonToObject = JsonUtils.jsonToObject(categoryByExample.getBody(), new TypeReference<StorageResult<List<Category>>>() {
		} );
		if(jsonToObject.getCode()!=200) {
			model.setViewName("error");
			model.addObject("msg",jsonToObject.getMsg());
		}else {
			model.setViewName("edgecategory");
			model.addObject("categories",jsonToObject.getResult());
		}

		return model;
	}

	@RequestMapping({"/order"})
	public ModelAndView order(ModelAndView model){
		ResponseEntity<String> categoryByExample = this.categoryService.findAll();
		StorageResult<List<Category>> jsonToObject = JsonUtils.jsonToObject(categoryByExample.getBody(), new TypeReference<StorageResult<List<Category>>>() {
		} );
		model.addObject("categories",jsonToObject);
		model.setViewName("order");
		return model;
	}
	@RequestMapping({"/orderDetail/{orderId}"})
	public ModelAndView orderDetail(@PathVariable Integer orderId, ModelAndView model){
		OrderWrap wrap = orderService.getInfoFromOrder(orderId);
		if(wrap.getCode()==500) {			
			model.setViewName("error?error="+wrap.getMsg());
			return model;
		}
		Float currencyRate = settingService.getSetting().getResult().getCurrencyRate();
		wrap.setTotalPrice(wrap.getTotalPrice()*currencyRate);
		List<CustomProduct> list = wrap.getList();
		for (CustomProduct customProduct : list) {
			customProduct.setSubtotal(customProduct.getSubtotal()*currencyRate);
			customProduct.getProduct().setSellingprice((int) (customProduct.getProduct().getSellingprice()*currencyRate));
		}
		model.addObject("order",wrap);
		model.addObject("customer",wrap.getCustomer());
		model.setViewName("orderDetail");
		return model;
	}
	@SuppressWarnings("unused")
	@RequestMapping({"/order_review"})
	public ModelAndView order_review(ModelAndView model,String order,Customer customer,RedirectAttributes  attrs){

		ResponseEntity<String> productByExample = this.productService.getProductByExample(order);
		StorageResult<OrderWrap> jsonToObject = JsonUtils.jsonToObject(productByExample.getBody(),new TypeReference<StorageResult<OrderWrap>>() {
		});
		
		OrderWrap result = jsonToObject.getResult();
		result.setCustomer(customer);
		
		OrderWrap wrap= orderService.creaOrder(jsonToObject.getResult());
		
		
		if(jsonToObject.isSuccess() && wrap.getCode()==200) {
/*			Float currencyRate = settingService.getSetting().getResult().getCurrencyRate();
			wrap.setTotalPrice(wrap.getTotalPrice()*currencyRate);
			List<CustomProduct> list = wrap.getList();
			for (CustomProduct customProduct : list) {
				customProduct.setSubtotal(customProduct.getSubtotal()*currencyRate);
				customProduct.getProduct().setSellingprice((int) (customProduct.getProduct().getSellingprice()*currencyRate));
			}*/
			/*model.addObject("order",wrap);*/
			model.addObject("customer",123);
			model.setViewName("redirect:/orderDetail/"+wrap.getOrder().getId());
		}else {
			if(jsonToObject.isSuccess()) {
				
				attrs.addAttribute("code", wrap.getCode());
				attrs.addAttribute("msg", wrap.getMsg());
				model.setViewName("redirect:/order");
			}else {
				attrs.addAttribute("code", jsonToObject.getCode());
				attrs.addAttribute("msg", jsonToObject.getMsg());
				model.setViewName("redirect:/order");
			}
		
		}
		return model;
	}


	@RequestMapping("/setting")
	public ModelAndView setting(ModelAndView model){
		StorageResult settingById = (StorageResult) this.settingService.getSetting(0);
		if(settingById.getResult()!=null) {
			model.addObject("setting",settingById.getResult());
		}
		ResponseEntity<String> findAll = vatService.findAll();
		
		StorageResult<List<Vat>> jsonToObject = JsonUtils.jsonToObject(findAll.getBody(),new TypeReference<StorageResult<List<Vat>>>() {
		});
		List<Vat> result = jsonToObject.getResult();
		if(jsonToObject.isSuccess() &&!jsonToObject.getResult().isEmpty()) {
			model.addObject("vats",result);			
		}
		model.setViewName("setting");
		return model;
	}
	@RequestMapping("/statement")
	public ModelAndView statement(ModelAndView model){

		model.setViewName("statement");
		return model;
	}
	@RequestMapping("/ordermanage")
	public ModelAndView ordermanage(ModelAndView model){

		model.setViewName("ordermanage");
		return model;
	}



}
