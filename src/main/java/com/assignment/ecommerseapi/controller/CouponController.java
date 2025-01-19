package com.assignment.ecommerseapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.ecommerseapi.dto.ApplicableCouponResponseDto;
import com.assignment.ecommerseapi.dto.CartDto;
import com.assignment.ecommerseapi.dto.CouponDto;
import com.assignment.ecommerseapi.dto.UpdatedCart;
import com.assignment.ecommerseapi.model.Coupon;
import com.assignment.ecommerseapi.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class CouponController {

	private final CouponService couponService;

	@PostMapping
	public ResponseEntity<Coupon> createCoupon(@RequestBody CouponDto dto) {
		return new ResponseEntity<>(couponService.createCoupon(dto), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Coupon> getCouponById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(couponService.getCoupon(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Coupon>> getAllCoupons() {
		return new ResponseEntity<>(couponService.getAllCoupons(), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable("id") Long id) {
		couponService.deleteCoupon(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/applicablecoupons")
	public ResponseEntity<List<ApplicableCouponResponseDto>> getApplicableCoupon(@RequestBody CartDto dto) {
		return new ResponseEntity<>(couponService.getApplicableCoupon(dto), HttpStatus.OK);
	}

	@PutMapping("/applycoupon/{id}")
	public ResponseEntity<UpdatedCart> applyCoupon(@PathVariable("id") Long id, @RequestBody CartDto dto) {
		return new ResponseEntity<>(couponService.applyCoupon(id, dto), HttpStatus.OK);
	}
}
