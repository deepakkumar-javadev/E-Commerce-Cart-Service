package com.deepak.cartService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.cartService.Entity.Cart;

public interface CartRepository  extends JpaRepository<Cart,Long>{

	Optional<Cart> findByUserId(Long userid);	
	 void deleteByUserId(Long userId);
	
	
}
