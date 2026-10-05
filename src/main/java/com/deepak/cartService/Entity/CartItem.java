package com.deepak.cartService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_items",
indexes = {
        @Index(name = "idx_cart_item_cart_id", columnList = "cart_id"),
        @Index(name = "idx_cart_item_product_id", columnList = "product_id"),
        @Index(name = "idx_cart_item_sku_code", columnList = "sku_code")
    }
		)
public class CartItem {

	@Id
	@GeneratedValue(strategy= GenerationType .IDENTITY)
	private Long id;
	
	private Long productId;
	private Integer quantity;
	private Double price;
	private String skuCode;
	
	@ManyToOne
	@JoinColumn(name="cart_id") 
	private Cart cart;     ///  this is foreign key for Cart class 
	
}
