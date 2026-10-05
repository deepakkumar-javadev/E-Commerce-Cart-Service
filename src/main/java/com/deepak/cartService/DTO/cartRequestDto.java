package com.deepak.cartService.DTO;

import lombok.Data;

@Data
public class cartRequestDto {

	private Long userId;
	private Long productId;
	private Integer quantity;
	
}
