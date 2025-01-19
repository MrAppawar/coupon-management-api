package com.assignment.ecommerseapi.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue("cartwisecoupon")
public class CartWiseCoupon extends Coupon {

	private double threshold;

	private double discount;
}
