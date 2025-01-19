package com.assignment.ecommerseapi.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
//@NoArgsConstructor
//@Entity
public class Product {

//	@Id
	private long productId;

	private int quantity;

}
