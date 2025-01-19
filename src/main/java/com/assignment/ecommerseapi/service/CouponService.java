package com.assignment.ecommerseapi.service;

import java.util.List;

import com.assignment.ecommerseapi.dto.ApplicableCouponResponseDto;
import com.assignment.ecommerseapi.dto.CartDto;
import com.assignment.ecommerseapi.dto.CouponDto;
import com.assignment.ecommerseapi.dto.UpdatedCart;
import com.assignment.ecommerseapi.model.Coupon;

public interface CouponService {

	List<Coupon> getAllCoupons();

	Coupon getCoupon(long couponId);

	Coupon createCoupon(CouponDto dto);

	void deleteCoupon(long couponId);

	List<ApplicableCouponResponseDto> getApplicableCoupon(CartDto cartDto);
	
	UpdatedCart applyCoupon(long id,CartDto cartDto);
	
}
