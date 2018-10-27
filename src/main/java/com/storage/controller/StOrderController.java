package com.storage.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.storage.entity.StOrder;
import com.storage.entity.custom.OrderTableItem;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.SettingRemoteService;
import com.storage.remote.service.StOrderRemoteService;
import com.storage.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/stOrder")
@Slf4j
public class StOrderController {
	
	@Autowired
	StOrderRemoteService service;
	@Autowired
	SettingRemoteService settingService;

	@PostMapping("/add")
	public Object addStOrder(StOrder stOrder) {

		return this.service.addStOrder(stOrder);
	}

	@GetMapping("/get/{id}")
	public Object getStOrder(@PathVariable(name = "id") Integer id) {

		return this.service.getStOrder(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteStOrderById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteStOrderById(id);
	}



	@PostMapping(value="/update",
			 consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,MediaType.APPLICATION_JSON_VALUE}, 
		        produces = {MediaType.APPLICATION_JSON_VALUE})	
	public Object updateStOrder(@RequestBody OrderTableItem item) {
	
		return this.service.updateStOrder(item);
	}
	
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}
	@GetMapping("/getStatistics")
	public Object getStatistics() {
		return this.service.getStatistics();
	}
	@GetMapping("/getAll")
	public Object getAll() {
	
		ResponseEntity<String> findAll = this.service.findAll();
		if(findAll.getStatusCodeValue()==200) {
			StorageResult<List<OrderTableItem>> jsonToObject = JsonUtils.jsonToObject(findAll.getBody(),new TypeReference<StorageResult<List<OrderTableItem>>>() {
			});			
			if(jsonToObject.isSuccess()) {
				List<OrderTableItem> result = jsonToObject.getResult();
				
				String objectToJson = JsonUtils.objectToJson(result,new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));			
				return "{\"data\":"+objectToJson+"}";				
			}
		}
		return "{\"data\":[]}";
	}


}
