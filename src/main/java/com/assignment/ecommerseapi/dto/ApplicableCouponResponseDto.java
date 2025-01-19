package com.assignment.ecommerseapi.dto;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ApplicableCouponResponseDto {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "couponId")
	private long couponId;

	@JsonProperty(value = "couponType")
	private String couponType;

	@JsonProperty(value = "discount")
	private double discount;
}
