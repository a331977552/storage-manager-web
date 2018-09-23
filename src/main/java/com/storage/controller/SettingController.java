package com.storage.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.storage.entity.Setting;
import com.storage.entity.Vat;
import com.storage.entity.custom.StorageResult;
import com.storage.entity.custom.VatWrapper;
import com.storage.remote.service.SettingRemoteService;
import com.storage.remote.service.VatRemoteService;

@Controller()
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired
	SettingRemoteService service;
	@Autowired
	VatRemoteService vatService;

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
	public ModelAndView updateSetting(ModelAndView and,  Setting setting,VatWrapper vats) {
		StorageResult<Setting> updateSetting = this.service.updateSetting(setting);
		ArrayList<Vat> vats2 = vats.getVats();
		for (Vat vat : vats2) {
			StorageResult<Vat> updateVat = vatService.updateVat(vat);
			if(!updateVat.isSuccess()) {
				and.setViewName("redirect:/setting?error="+updateSetting.getMsg());
				return and;
			}
		}
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
