package com.assignment.ecommerseapi.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.assignment.ecommerseapi.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CouponDto implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "couponId")
	private long couponId;

	@JsonProperty(value = "couponType")
	private String couponType;

	@JsonProperty(value = "threshold")
	private double threshold;

	@JsonProperty(value = "discount")
	private double discount;

	@JsonProperty(value = "productId")
	private Long productId;

	@JsonProperty(value = "buyProducts")
	private List<Product> buyProducts;

	@JsonProperty(value = "getProducts")
	private List<Product> getProducts;

	@JsonProperty(value = "repetitionLimit")
	private int repetitionLimit;

}
