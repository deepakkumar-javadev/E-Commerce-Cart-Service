package com.deepak.cartService.DTO;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateQuantityRequest {

	@Min(1)
	private Integer quantity;
}
