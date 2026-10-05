package com.deepak.cartService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deepak.cartService.DTO.CartItemResponseDto;
import com.deepak.cartService.DTO.InventoryResDto;
import com.deepak.cartService.DTO.UpdateQuantityRequest;
import com.deepak.cartService.DTO.cartRequestDto;
import com.deepak.cartService.DTO.cartResponseDto;
import com.deepak.cartService.DTO.productResponse;
import com.deepak.cartService.Entity.Cart;
import com.deepak.cartService.Entity.CartItem;
import com.deepak.cartService.Repository.CartItemRepository;
import com.deepak.cartService.Repository.CartRepository;
import com.deepak.cartService.client.InventoryClient;
import com.deepak.cartService.client.ProductClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class cartServiceImpl implements cartService {

	
	@Value("${internal.service.token}")
	private String internalServiceToken;
	
	// to save cart details to database
	private final CartRepository cartRepo;

	private final CartItemRepository cartItemRepo;
	// to get product details
	private final ProductClient productClient;

	// to get stock details from Inventory Service
	private final InventoryClient inventoryClient;
	
	
	// logic for addtocart

	@Override
	public cartResponseDto addToCart(cartRequestDto req) {

		// 1. Validate quantity
		if (req.getQuantity() <= 0) {
			throw new RuntimeException("Quantity must be greater than 0");
		}

		// 2. Get product details from Product Service

		productResponse product = productClient.getProductDetails(req.getProductId());

		// 3. Find cart by userId
		// If cart doesn't exist, create a new cart
		Cart createdCart = cartRepo.findByUserId(req.getUserId()).orElseGet(() -> {

			Cart cart = new Cart();
			cart.setUserId(req.getUserId());
			cart.setCreatedAt(LocalDateTime.now());
			cart.setUpdatedAt(LocalDateTime.now());

			return cartRepo.save(cart);
		});

		// 4. Check whether product already exists in this cart
		CartItem item = cartItemRepo.findByCartAndProductId(createdCart, req.getProductId()).orElse(null);

		// =====================================================
		// 5. PRODUCT ALREADY EXISTS
		// =====================================================
		if (item != null) {

			// Existing quantity + new requested quantity
			//
			// Example:
			// Existing = 2
			// Request = 2
			// New = 4
			//
			// Again +2
			// New = 6

			int newQuantity = item.getQuantity() + req.getQuantity();

			item.setQuantity(newQuantity);

			cartItemRepo.save(item);
		}

		// =====================================================
		// 6. PRODUCT DOES NOT EXIST
		// =====================================================
		else {

			item = new CartItem();

			item.setProductId(product.getProductid());
			item.setQuantity(req.getQuantity());
			item.setPrice(product.getPrice());
			item.setCart(createdCart);
			item.setSkuCode(product.getSkuCode());

			cartItemRepo.save(item);
		}

		// =====================================================
		// 7. UPDATE CART TIME
		// =====================================================

		createdCart.setUpdatedAt(LocalDateTime.now());
		cartRepo.save(createdCart);

		// =====================================================
		// 8. CREATE ITEM RESPONSE
		// =====================================================

		CartItemResponseDto itemRes = new CartItemResponseDto();

		itemRes.setProductId(item.getProductId());
		itemRes.setProductName(product.getName());
		itemRes.setQuantity(item.getQuantity());
		itemRes.setPrice(item.getPrice());
		itemRes.setSkuCode(item.getSkuCode());

		// =====================================================
		// 9. CREATE CART RESPONSE
		// =====================================================

		cartResponseDto cartres = new cartResponseDto();

		cartres.setCartId(createdCart.getId());
		cartres.setUserId(req.getUserId());
		cartres.setItems(List.of(itemRes));

		return cartres;
	}

	// 2. logic for get cartinfo

	@Override
	public cartResponseDto getCart(Long userid) {

		// 1. get cart
		Cart cart = cartRepo.findByUserId(userid).orElseThrow(() -> new RuntimeException("CART_NOT_FOUND"));

		// 2. create item response list
		List<CartItemResponseDto> ItemsResponselist = new ArrayList<>();

		// 3. fetch cart items from cart_item table
		List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

		// 4. get each cart item
		for (CartItem item : cartItems) {

			productResponse product = productClient.getProductDetails(item.getProductId());

			CartItemResponseDto cartItemRes = new CartItemResponseDto();

			cartItemRes.setProductId(product.getProductid());
			cartItemRes.setProductName(product.getName());
			cartItemRes.setQuantity(item.getQuantity());
			cartItemRes.setPrice(item.getPrice());
			cartItemRes.setSkuCode(item.getSkuCode());

			ItemsResponselist.add(cartItemRes);
		}

		// 5. create cart response
		cartResponseDto cartRes = new cartResponseDto();

		cartRes.setCartId(cart.getId());
		cartRes.setUserId(cart.getUserId());
		cartRes.setItems(ItemsResponselist);

		return cartRes;
	}

	// 3. logic for remove all item

	@Override
	public void removeCartItem(Long userId, Long productId) {

		// 1 find cart by user id
		Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("cart not found..."));

		// 2 find cart item by userid and productid

		CartItem cartitem = cartItemRepo.findByCartAndProductId(cart, productId)
				.orElseThrow(() -> new RuntimeException("product not exists"));

		// delete cartitem from cartitemRepo
		cartItemRepo.delete(cartitem);

		// update time & date and save cartdb
		cart.setUpdatedAt(LocalDateTime.now());
		cartRepo.save(cart);

	}

	// 4 remove single item
	@Override
	public void removeitem(Long UserId) {

		// 1 find cart by user id
		Cart cart = cartRepo.findByUserId(UserId).orElseThrow(() -> new RuntimeException("cart not found..."));

		// 2 find cart item by userid and productid

		List<CartItem> cartitem = cartItemRepo.findByCartId(cart.getId());

		// delete cartitem from cartitemRepo
		cartItemRepo.deleteAll(cartitem);

		cart.setUpdatedAt(LocalDateTime.now());
		cartRepo.save(cart);

	}

	// 5. UPDATE QUANTITY
	@Override
	public cartResponseDto updateQuantity(Long userId, Long productId, UpdateQuantityRequest request) {

	    if (request.getQuantity() == null || request.getQuantity() < 1) {
	        throw new RuntimeException("Quantity must be greater than 0");
	    }

	    Cart cart = cartRepo.findByUserId(userId)
	            .orElseThrow(() -> new RuntimeException("Cart not found"));

	    CartItem item = cartItemRepo.findByCartAndProductId(cart, productId)
	            .orElseThrow(() -> new RuntimeException("Product not found in cart"));

	    // Get latest product details
	    productResponse product = productClient.getProductDetails(productId);

	    // Get SKU from cart item
	    String skuCode = item.getSkuCode();

	    if (skuCode == null || skuCode.isBlank()) {
	        throw new RuntimeException("SKU code not found for product: " + productId);
	    }

	    // Get actual stock from Inventory Service
	    InventoryResDto inventory = inventoryClient.getInventoryStock(skuCode);

	    if (inventory == null) {
	        throw new RuntimeException("Inventory not found for SKU: " + skuCode);
	    }

	    // Actual available stock
	    int availableStock = inventory.getStockQuantity();

	    // Check requested quantity
	    if (request.getQuantity() > availableStock) {
	        throw new RuntimeException(
	                "Requested quantity not available. Available stock: " + availableStock);
	    }

	    // Update cart quantity
	    item.setQuantity(request.getQuantity());
	    cartItemRepo.save(item);

	    cart.setUpdatedAt(LocalDateTime.now());
	    cartRepo.save(cart);

	    // Response
	    CartItemResponseDto itemResponse = new CartItemResponseDto();

	    itemResponse.setProductId(item.getProductId());
	    itemResponse.setProductName(product.getName());
	    itemResponse.setQuantity(item.getQuantity());
	    itemResponse.setPrice(item.getPrice());
	    itemResponse.setSkuCode(item.getSkuCode());

	    cartResponseDto cartResponse = new cartResponseDto();

	    cartResponse.setCartId(cart.getId());
	    cartResponse.setUserId(cart.getUserId());
	    cartResponse.setItems(List.of(itemResponse));

	    return cartResponse;
	}

	// clear cart...
	@Override
	public void clearCart(Long userId) {

		Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));

		// remove items
		cart.getItems().clear();

		// updated time
		cart.setUpdatedAt(LocalDateTime.now());

		// save repository..
		cartRepo.save(cart);

	}

}
