package com.deepak.cartService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.deepak.cartService.DTO.orderResponseDto;
import com.deepak.cartService.config.FeignConfig;


@FeignClient(name = "ECOM-ORDER-SERVICE", url = "http://localhost:8086" , configuration = FeignConfig.class)
public interface OrderClient {

	@GetMapping("/orders/getorder/{id}")
	public orderResponseDto getOrderById(@PathVariable Long id);

}
