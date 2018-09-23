package com.storage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Advertisement;
import com.storage.remote.service.AdvertisementServiceRemote;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/ad")
@Slf4j
public class AdvertisementController {

	@Autowired
	AdvertisementServiceRemote service;
	
	@RequestMapping("/add")
	public Advertisement addAdvertisement( Advertisement ad) {
		return service.addAdvertisement(ad);
	}

	@RequestMapping("/update")
	public Advertisement updateAdvertisement( Advertisement ad){
		if(ad.getId()==null) {
			return service.addAdvertisement(ad);
		}else{
			service.updateAdvertisement(ad);	
			return null;
		}
	}

	@RequestMapping("/delete")
	public void deleteAdvertisement( Advertisement ad){
		service.deleteAdvertisement(ad);
	}

	@RequestMapping("/get/{direction}")
	public List<Advertisement> findAdvertisementByPosition(@PathVariable("direction")Integer direction){
		return service.findAdvertisementByPosition(direction);
	}
	
	
	
	
	
}
