package com.storage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.storage.entity.Category;
import com.storage.entity.Product;
import com.storage.entity.ProductExample;
import com.storage.entity.ProductExample.Criteria;
import com.storage.entity.Productimg;
import com.storage.entity.Vat;
import com.storage.entity.custom.CustomProduct;
import com.storage.entity.custom.SeaWeedFileSystem;
import com.storage.entity.custom.StorageResult;
import com.storage.service.CategoryService;
import com.storage.service.ProductService;
import com.storage.service.VatService;
import com.storage.utils.JsonUtils;
import com.storage.utils.StringUtils;

@Controller()
@RequestMapping("/product")
@PropertySource("classpath:myapp.properties")
public class ProductController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService service;
	@Autowired
	VatService vatService;
	@Value("${seaweeddfs.url}")
	String seaweeddfsUrl;

	
	/*	@PostMapping("/add")
	public Object addProduct(Product product) {

		return this.service.addProduct(product);
	}*/
	@PostMapping("/add")
	@ResponseBody()
	public Object addProductWithImg( CustomProduct product,@RequestParam(value="file",required=false) MultipartFile file) {



		if(file==null ||file.isEmpty())
			return StorageResult.failed("not img present");
		String dir = addImg(file);
		if(dir.startsWith("error"))
			return StorageResult.failed(dir);
		
		List<Productimg> list=new ArrayList<>();
		Productimg productimg = new Productimg();
		productimg.setUrl(dir);
		list.add(productimg);
		product.setList(list);
		return this.service.addProduct(product);
	}



	public String addImg(MultipartFile file) {
		
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(seaweeddfsUrl+":9333/dir/assign").get().build();
		try {
			//request server to get fid
			Response response = okHttpClient.newCall(request).execute();
			if(response.isSuccessful()) {
				 String body = response.body().string();
				 SeaWeedFileSystem jsonToPojo = JsonUtils.jsonToPojo(body, SeaWeedFileSystem.class);
				 String fid = jsonToPojo.getFid();
				 //get suffix of the orignal file
				 String originalFilename = file.getOriginalFilename();
				 int lastIndexOf = originalFilename.lastIndexOf('.');
				 
				 String suffix = originalFilename.substring(lastIndexOf,originalFilename.length());
				 
				 String fileUrl= seaweeddfsUrl+":8080/"+fid;
				 //file body
				 com.squareup.okhttp.RequestBody fileBody = com.squareup.okhttp.RequestBody.create(MediaType.parse("image/png"), file.getBytes());
				 //file body with name and file name on 
				 com.squareup.okhttp.RequestBody bodyWithPicture = new MultipartBuilder().
						 addPart(Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + fid+suffix + "\""),
								 fileBody).build();
				 //request
				  request = new Request.Builder().url(fileUrl).post(bodyWithPicture).build();
				  //execute request
				  Response responseFile = okHttpClient.newCall(request).execute();
				 if(responseFile.isSuccessful())
					 return fid+suffix;
				 else {
					 return "error: when inserting img into img server";
				 }
			}else {
				return "error:"+response.body().string();	
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "error: okHttpClient";
		}
		
	}



	@GetMapping("/get/{id}")
	public Object getProduct(ModelAndView model,@PathVariable(name = "id") Integer id) {
		StorageResult productById = this.service.getProductById(id);
		if(productById.getResult()==null) {
			model.setViewName("redirect:/index");
		}else {
			CustomProduct result = (CustomProduct) productById.getResult();
			model.addObject("product",result.getProduct());
			model.addObject("imgs",result.getList());
			Category category=new Category();
			StorageResult categoryByExample = this.categoryService.getCategoryByExample(category);
			StorageResult vatByExample = vatService.getVatByExample(new Vat());
			model.addObject("categories",categoryByExample.getResult());
			model.addObject("vats",vatByExample.getResult());
			model.setViewName("productdetails");
		}
		return model;
	}

	@RequestMapping("/barcode/get")
	@ResponseBody
	public Object getProductByBarcode(@RequestParam(required=false,name="barcode",value="barcode") String barcode,
			@RequestParam(required=false,name="id",value="id") Integer id) {

		if(StringUtils.isEmpty(barcode) && (id==null|| id<0))
			return StorageResult.failed("barcode or product id is required");
		StorageResult productById;
		if(!StringUtils.isEmpty(barcode)) {
			productById = this.service.getProductByBarCode(barcode);
		}else {
			productById=this.service.getProductById(id);
		}
		if(productById.getResult()==null)
			return StorageResult.failed("didnot find the item");
		else
			return productById;

	}

	@RequestMapping("/list")
	@ResponseBody
	public Object getProduct(@RequestBody Product product,Integer currentPage,Integer pageSize) {

		/*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {121311260004
			e.printStackTrace();
		}*/
		return this.service.getProductByExample(product,currentPage,pageSize);
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
	public Object updateProduct( CustomProduct product,@RequestParam(value="file",required=false) MultipartFile file) {	
		if(file!=null) {
			String dir = addImg(file);
			if(dir.startsWith("error"))
				return StorageResult.failed(dir);
			List<Productimg> list=new ArrayList<>();
			Productimg e=new Productimg();
			e.setUrl(dir);
			e.setProdcutid(product.getProduct().getId());
			list.add(e);
			product.setList(list);		
		}else {
			product.setList(null);			
		}
		return this.service.updateProduct(product);
	}

	@RequestMapping("/getName/{categoryId}.json")
	@ResponseBody
	public Object getProductNamesByCategory(@PathVariable(name="categoryId") Integer categoryId) {

		return this.service.getProductNamesByCategory(categoryId).getResult();
	}

	@GetMapping("/count")
	@ResponseBody
	public Object count(@RequestBody Product product) {
		ProductExample example=new ProductExample();
		Criteria criteria = example.createCriteria();
		if(product!=null) {
			if(product.getId()!=null){
				criteria.andIdEqualTo(product.getId());
			}

			if(product.getName()!=null){
				criteria.andNameLike("%"+product.getName()+"%");
			}

			if(product.getQuantity()!=null){
				criteria.andQuantityEqualTo(product.getQuantity());
			}

			if(product.getBuyingprice()!=null){
				criteria.andBuyingpriceEqualTo(product.getBuyingprice());
			}

			if(product.getSellingprice()!=null){
				criteria.andSellingpriceEqualTo(product.getSellingprice());
			}

			if(product.getCreatedtime()!=null){
				criteria.andCreatedtimeEqualTo(product.getCreatedtime());
			}

			if(product.getSupplier()!=null){
				criteria.andSupplierEqualTo(product.getSupplier());
			}

			if(product.getVat()!=null){
				criteria.andVatEqualTo(product.getVat());
			}

			if(product.getUpdatetime()!=null){
				criteria.andUpdatetimeEqualTo(product.getUpdatetime());
			}
			if(product.getCategory()!=null && product.getCategory()!=-1){
				criteria.andCategoryEqualTo(product.getCategory());
			}
		}
		return this.service.count(example);
	}

	/*	@ExceptionHandler(value=SizeLimitExceededException.class)
	@ResponseBody
	public StorageResult handleException(Exception exception) {
		System.out.println("handleException22:  "+ exception.getMessage());
		return StorageResult.failed("file is too large, size cannot exceed 5MB");

	}*/


}
