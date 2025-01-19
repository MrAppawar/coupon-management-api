package com.assignment.ecommerseapi.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue("productwisecoupon")
public class ProductWiseCoupon extends Coupon{

	private Long productId;

	private double discount;
}
