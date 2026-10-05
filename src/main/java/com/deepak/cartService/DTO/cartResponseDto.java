package com.deepak.cartService.DTO;

import java.util.List;

import lombok.Data;

@Data
public class cartResponseDto {

	private Long cartId;
	private Long userId;
	private List<CartItemResponseDto> items;
}
