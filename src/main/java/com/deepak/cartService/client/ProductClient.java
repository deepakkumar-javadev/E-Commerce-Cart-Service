package com.deepak.cartService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.deepak.cartService.DTO.productResponse;
import com.deepak.cartService.config.FeignConfig;

@FeignClient(name = "ECOM-PRODUCT-SERVICE", url = "http://localhost:8083",configuration = FeignConfig.class)
public interface ProductClient {

	@GetMapping("/product/get/{productId}")
	public productResponse getProductDetails(@PathVariable Long productId);

}
