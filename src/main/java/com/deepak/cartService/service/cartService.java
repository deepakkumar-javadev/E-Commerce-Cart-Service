package com.deepak.cartService.service;

import com.deepak.cartService.DTO.UpdateQuantityRequest;
import com.deepak.cartService.DTO.cartRequestDto;
import com.deepak.cartService.DTO.cartResponseDto;
import com.deepak.cartService.Entity.Cart;

public interface cartService {

	
	public cartResponseDto addToCart(cartRequestDto req);
	public cartResponseDto getCart(Long userid);
	public  void removeCartItem(Long userId, Long productId);
	public cartResponseDto updateQuantity(Long userId, Long productId, UpdateQuantityRequest request);
	public void removeitem(Long UserId);
	
	public void clearCart(Long userId);

	
}
