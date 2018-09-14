package com.storage.remote.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.storage.config.FeignConfiguration;
import com.storage.entity.Customer;
import com.storage.entity.Setting;
import com.storage.entity.StOrder;
import com.storage.entity.custom.OrderWrap;
import com.storage.entity.custom.StorageResult;

@FeignClient(value="back-service")
public interface StOrderRemoteService {
	@PostMapping("/stOrder/add")
	StorageResult<StOrder>  addStOrder(StOrder stOrder);
	@GetMapping("/stOrder/get/{id}")
	StorageResult<StOrder>  getStOrder(@PathVariable(name = "id")Integer id);

	@GetMapping("/stOrder/delete/{id}")
	StorageResult<StOrder>  deleteStOrderById(@PathVariable(name = "id")Integer id);
	@PostMapping("/stOrder/update")
	StorageResult<StOrder>  updateStOrder(StOrder stOrder);
	@GetMapping("/stOrder/count")
	StorageResult<Long>  count();
	@RequestMapping("/stOrder/createOrder")
	OrderWrap  creaOrder(@RequestBody()OrderWrap result);

}