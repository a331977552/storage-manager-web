package com.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storage.entity.Vat;
import com.storage.service.VatService;

@RestController()
@RequestMapping("/vat")
public class VatController {

	@Autowired
	VatService service;

	@PostMapping("/add")
	public Object addVat(Vat vat) {

		return this.service.addVat(vat);
	}

	@GetMapping("/get/{id}")
	public Object getVat(@PathVariable(name = "id") Integer id) {

		return this.service.getVatById(id);
	}

	@GetMapping("/delete/{id}")
	public Object deleteVatById(@PathVariable(name = "id") Integer id) {
		return this.service.deleteVatById(id);
	}



	@PostMapping("/update")
	public Object updateVat(Vat vat) {
		return this.service.updateVat(vat);
	}
	@GetMapping("/count")
	public Object count() {
		return this.service.count();
	}


}
