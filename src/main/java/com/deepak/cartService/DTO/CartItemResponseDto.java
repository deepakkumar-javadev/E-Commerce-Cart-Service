package com.deepak.cartService.DTO;

import lombok.Data;

@Data
public class CartItemResponseDto {

	private Long productId;
	private String productName;
	private Integer quantity;
	private Double price;
	private String skuCode; 
	
}
