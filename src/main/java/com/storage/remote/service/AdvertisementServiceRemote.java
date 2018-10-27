package com.storage.remote.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.storage.entity.Advertisement;

@FeignClient("back-service")
public interface AdvertisementServiceRemote {
	
	@RequestMapping("/ad/add")
	public Advertisement addAdvertisement(@RequestBody Advertisement ad);

	@RequestMapping("/ad/update")
	public ResponseEntity<String> updateAdvertisement(@RequestBody Advertisement ad);

	@RequestMapping("/ad/delete")
	public void deleteAdvertisement(@RequestBody Advertisement ad);

	@RequestMapping("/ad/get/{direction}")
	public List<Advertisement> findAdvertisementByPosition(@PathVariable("direction")Integer direction);
	
	
	
}
