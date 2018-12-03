package com.storage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.storage.entity.Product;
import com.storage.entity.ProductDetail;
import com.storage.entity.Productimg;
import com.storage.entity.custom.CustomProduct;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.CategoryRemoteService;
import com.storage.remote.service.ProductRemoteService;
import com.storage.remote.service.ProductimgRemoteService;
import com.storage.remote.service.SettingRemoteService;
import com.storage.remote.service.VatRemoteService;
import com.storage.utils.JsonUtils;
import com.storage.utils.StringUtils;

@Controller()
@RequestMapping("/product")
public class ProductController {

	@Autowired
	CategoryRemoteService categoryService;

	@Autowired
	ProductRemoteService service;

	@Autowired
	ProductimgRemoteService imgservice;
	@Autowired
	SettingRemoteService settingRemoteService;
	@Autowired
	VatRemoteService vatService;

	/*
	 * @PostMapping("/add") public Object addProduct(Product product) {
	 * 
	 * return this.service.addProduct(product); }
	 */
	@PostMapping("/add")
	@ResponseBody()
	public Object addProductWithImg(CustomProduct product,@RequestParam(value="file",required=false)MultipartFile file
		) {
		if(file==null)
			return StorageResult.failed("img is not presented");
		StorageResult<Productimg> addImg = imgservice.addImg(file);
		if(!addImg.isSuccess())
			return addImg;
		List<Productimg> list = new ArrayList<>();
		Productimg result = addImg.getResult();
		list.add(result);
		product.setImgs(list);
		StorageResult<Product> addProductWithImg = service.addProductWithImg(product);
		return addProductWithImg;
	}

	@GetMapping("/get/{id}")
	public ModelAndView getProduct(ModelAndView model, @PathVariable(name = "id") Integer id) {
		ResponseEntity<String> productById = this.service.getProduct(id);
		if (productById.getBody() == null) {
			model.setViewName("redirect:/index");
		} else {
			String body = productById.getBody();
			ProductDetail jsonToPojo = JsonUtils.jsonToPojo(body, ProductDetail.class);
			model.addObject("product", jsonToPojo.getProduct());
			model.addObject("imgs", jsonToPojo.getImgs());
			model.addObject("category", jsonToPojo.getCategory());
			model.addObject("vats", jsonToPojo.getVats());
			model.setViewName("productdetails");

		}
		return model;
	}

	@RequestMapping("/barcode/get")
	@ResponseBody
	public Object getProductByBarcode(
			@RequestParam(required = false, name = "barcode", value = "barcode") String barcode,
			@RequestParam(required = false, name = "id", value = "id") Integer id) {

		if (StringUtils.isEmpty(barcode) && (id == null || id < 0))
			return StorageResult.failed("barcode or product id is required");
		ResponseEntity<String>  productById;
		if (!StringUtils.isEmpty(barcode)) {
			productById = this.service.getProductByBarcode(barcode, id);
		} else {
			productById =this.service.getProduct(id);
		}
		StorageResult<Product> jsonToObject = JsonUtils.jsonToObject(productById.getBody(),new TypeReference<StorageResult<Product>>() {
		});
		if (jsonToObject.getResult() == null)
			return StorageResult.failed("didnot find the item");
		else
			return jsonToObject;

	}

	@RequestMapping("/list")
	@ResponseBody
	public Object getProduct(@RequestBody Product product, Integer currentPage, Integer pageSize,String sort,String sort_order) {

		/*
		 * try { Thread.sleep(3000); } catch (InterruptedException e) {121311260004
		 * e.printStackTrace(); }
		 */
		product.setSort(sort);
		product.setSort_order(sort_order);
		ResponseEntity<String> product2 = this.service.getProduct(product, currentPage, pageSize);

		
		return product2.getBody();
	}

	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public Object deleteProductById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteProductById(id);
	}

	@GetMapping("/stockreminder")
	@ResponseBody
	public Object getStockReminder() {
		return this.service.getStockReminder();
	}

	@PostMapping("/update")
	@ResponseBody
	public Object updateProduct(CustomProduct product,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		List<Productimg> list=new ArrayList<>();
		if(file!=null) {
			StorageResult<Productimg> addProductimg = imgservice.addImg(file);
			if(addProductimg.isSuccess()) {
				list.add(addProductimg.getResult());				
			}			
		}
		product.setImgs(list);
		return this.service.updateProduct(product);
	}

	@RequestMapping("/getName/{categoryId}.json")
	@ResponseBody
	public Object getProductNamesByCategory(@PathVariable(name = "categoryId") Integer categoryId) {
		ResponseEntity<String> result = this.service.getProductNamesByCategory(categoryId);

		return result.getBody();
	}

	@GetMapping("/count")
	@ResponseBody
	public Object count(@RequestBody Product product) {

		return this.service.count(product);
	}

	/*
	 * @ExceptionHandler(value=SizeLimitExceededException.class)
	 * 
	 * @ResponseBody public StorageResult handleException(Exception exception) {
	 * System.out.println("handleException22:  "+ exception.getMessage()); return
	 * StorageResult.failed("file is too large, size cannot exceed 5MB");
	 * 
	 * }
	 */

}
