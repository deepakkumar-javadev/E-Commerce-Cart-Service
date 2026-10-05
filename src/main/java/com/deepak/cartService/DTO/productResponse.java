package com.deepak.cartService.DTO;

import lombok.Data;

@Data
public class productResponse {

	private Long productid;
    private String name;
    private double price;
    private String brand;
    private String color;
    private String size;
    private String category;
    private String availabilitystatus;
	private int stockQuantity;
	private String discription;
	private String skuCode;
}
