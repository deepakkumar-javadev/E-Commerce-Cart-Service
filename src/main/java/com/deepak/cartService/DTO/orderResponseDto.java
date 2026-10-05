package com.deepak.cartService.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class orderResponseDto {


    // Order Details
	private Long userId;
    private Long orderId;
    private String orderNumber;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
   
    // Payment Details
    private String paymentMethod;
    private PaymentStatus paymentStatus;
 
    

    // Order Items
    private List<OrderItemResponseDto> items;

    // Message
    private String msg;
    
}
