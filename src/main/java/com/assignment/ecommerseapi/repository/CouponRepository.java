package com.assignment.ecommerseapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.ecommerseapi.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
