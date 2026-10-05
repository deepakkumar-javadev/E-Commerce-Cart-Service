package com.deepak.cartService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.cartService.DTO.UpdateQuantityRequest;
import com.deepak.cartService.DTO.cartRequestDto;
import com.deepak.cartService.DTO.cartResponseDto;
import com.deepak.cartService.service.cartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class cartcontroller {

	private final cartService service;

	//## CUSTOMER API

	// 1. CREATE CART
	@PostMapping("/createcart")
	public ResponseEntity<cartResponseDto> createCart(@RequestBody cartRequestDto req) {

		cartResponseDto cartres = service.addToCart(req);

		return ResponseEntity.ok(cartres);
	}

	// 2.GET CART
	@GetMapping("/getcart/{userid}")
	public ResponseEntity<cartResponseDto> getCart(@PathVariable Long userid) {

		cartResponseDto cart = service.getCart(userid);

		return ResponseEntity.ok().body(cart);
	}

	// 3. REMOVE ITEM FROM CART 

	@DeleteMapping("/removecart/cart/{userId}/items/{productId}")
	public ResponseEntity<String> RemoveItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {

		service.removeCartItem(userId, productId);

		return ResponseEntity.ok("Product deleted successfully...");
	}

	// 4. UPDATE QUANTITY OF CARTITEM
	@PatchMapping("/updatefield/{userId}/items/{productId}")
	public ResponseEntity<cartResponseDto> updateQuantity(@PathVariable Long userId, @PathVariable Long productId,
			@Valid @RequestBody UpdateQuantityRequest request) {

		cartResponseDto response = service.updateQuantity(userId, productId, request);

		return ResponseEntity.ok(response);
	}

	
	//## INTERNAL/ FEIGNCLIENT
	
	// CLEAR CART
	@DeleteMapping("/clear/{userId}")
	public ResponseEntity<?> clearCart(@PathVariable Long userId, @RequestHeader("X-Internal-Token") String internalToken) {

		service.clearCart(userId);

		return ResponseEntity.ok("Cart cleared successfully");
	}
	
	
	
	//. REMOVE CART  [ADMIN]
		@DeleteMapping("/removecart/cart/{userId}")
		public ResponseEntity<Void> RemovecartItem(@PathVariable Long userId) {

			service.removeitem(userId);

			return ResponseEntity.ok().build();
		}
}
