package com.deepak.cartService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.cartService.Entity.Cart;
import com.deepak.cartService.Entity.CartItem;

public interface CartItemRepository  extends JpaRepository<CartItem,Long>{

	Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
	List<CartItem> findByCartId(Long id);
	
}
