package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.storage.entity.Setting;
import com.storage.entity.custom.StorageResult;
import com.storage.remote.service.SettingRemoteService;

@Controller()
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired
	SettingRemoteService service;

	@PostMapping("/add")
	public Object addSetting(Setting setting) {

		return this.service.addSetting(setting);
	}

	@GetMapping("/get/{id}")
	public Object getSetting(@PathVariable(name = "id") Integer id) {
		return this.service.getSetting(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteSettingById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteSettingById(id);
	}



	@PostMapping("/update")
	public ModelAndView updateSetting(ModelAndView and,  Setting setting) {
		StorageResult<Setting> updateSetting = this.service.updateSetting(setting);
		if(updateSetting.isSuccess()) {
			and.setViewName("redirect:/success?msg=success");
		
		}else {
			and.setViewName("redirect:/setting?error="+updateSetting.getMsg());			
		}
		return and;
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}



}
