package com.storage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.storage.entity.Carousel;
import com.storage.entity.Productimg;
import com.storage.entity.custom.CustomCarousel;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.CarouselRemoteService;
import com.storage.remote.service.ProductimgRemoteService;

@RestController()
public class CarouselController {

	@Autowired
	CarouselRemoteService service;
	@Autowired
	ProductimgRemoteService imgservice;
	@PostMapping("/carousel/add")
	public StorageResult<Carousel> addCarousel(Carousel carousel,
			@RequestParam(value = "file", required = false)MultipartFile file){
		StorageResult<Productimg> addImg = imgservice.addWithFullURL(file);
		if(addImg.isSuccess()) {
			String url = addImg.getResult().getUrl();
			carousel.setPic_url(url);
			return service.addCarousel(carousel);
			
		}else {
			return StorageResult.failed(addImg.getMsg());
		}
		
	}
	@PostMapping(value="/carousel/update")
	public StorageResult<Carousel> updateCarousel( CustomCarousel carousel){
		Carousel carousel2=new Carousel();
		carousel2.setId(carousel.getPk());
		if(carousel.getName().equals("title")) {
			carousel2.setTitle(carousel.getValue());
		}
		if(carousel.getName().equals("pic_url")) {
			carousel2.setPic_url(carousel.getValue());
		}
		if(carousel.getName().equals("redirect_url")) {
			carousel2.setRedirect_url(carousel.getValue());
		}
		
		return service.updateCarousel(carousel2);
	}
	@DeleteMapping("/carousel/delete/{id}")
	public StorageResult<String> deleteCarousel(@PathVariable("id") Integer id){
		return service.deleteCarousel(id);
	}
	@GetMapping("/carousel/get/{id}")
	public StorageResult<Carousel> getCarousel(@PathVariable("id") Integer id){
		return service.getCarousel(id);
	}
	@GetMapping("/carousel/findAll")
	public  StorageResult<List<Carousel>> getAllCarousel(){
		return service.getAllCarousel();
	}
	@GetMapping("/carousel/findByExample")
	public StorageResult<List<Carousel>> getAllCarousel(@RequestBody Carousel  carousel){
		return service.getAllCarousel( carousel);
	}
	
	
	
}
