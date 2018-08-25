package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Setting;
import com.storage.service.SettingService;

@RestController()
@RequestMapping("/setting")
public class SettingController {

	@Autowired
	SettingService service;

	@PostMapping("/add")
	public Object addSetting(Setting setting) {

		return this.service.addSetting(setting);
	}

	@GetMapping("/get/{id}")
	public Object getSetting(@PathVariable(name = "id") Integer id) {

		return this.service.getSettingById(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteSettingById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteSettingById(id);
	}



	@PostMapping("/update")
	public Object updateSetting(Setting setting) {
		return this.service.updateSetting(setting);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}



}
