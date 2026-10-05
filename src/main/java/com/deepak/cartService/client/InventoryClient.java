package com.deepak.cartService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.deepak.cartService.DTO.InventoryResDto;
import com.deepak.cartService.config.FeignConfig;

@FeignClient(
        name = "ECOM-INVENTORY-SERVICE",
        url = "http://localhost:8084",
        configuration = FeignConfig.class
)
public interface InventoryClient {

    @GetMapping("/stock/getstock/{skuCode}")
    InventoryResDto getInventoryStock(@PathVariable String skuCode);
}