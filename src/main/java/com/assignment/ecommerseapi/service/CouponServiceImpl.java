package com.assignment.ecommerseapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.ecommerseapi.dto.ApplicableCouponResponseDto;
import com.assignment.ecommerseapi.dto.CartDto;
import com.assignment.ecommerseapi.dto.CouponDto;
import com.assignment.ecommerseapi.dto.UpdatedCart;
import com.assignment.ecommerseapi.exception.CouponNotFoundException;
import com.assignment.ecommerseapi.model.BXGY;
import com.assignment.ecommerseapi.model.CartWiseCoupon;
import com.assignment.ecommerseapi.model.Coupon;
import com.assignment.ecommerseapi.model.Item;
import com.assignment.ecommerseapi.model.Product;
import com.assignment.ecommerseapi.model.ProductWiseCoupon;
import com.assignment.ecommerseapi.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class CouponServiceImpl implements CouponService {

	private final CouponRepository repository;

	private static final String STRING_MESSAGE = "Not Found Coupon with ID ";

	private static final String CART_WISE_COUPON = "CARTWISECOUPON";

	private static final String PRODUCT_WISE_COUPON = "PRODUCTWISECOUPON";

	private static final String BXGY_COUPON = "BXGYCOUPON";

	@Override
	public List<Coupon> getAllCoupons() {
		return repository.findAll();
	}

	@Override
	public Coupon getCoupon(long couponId) {
		Optional<Coupon> couponEntity = repository.findById(couponId);
		if (couponEntity.isPresent()) {
			return couponEntity.get();
		} else {
			throw new CouponNotFoundException(STRING_MESSAGE + couponId);
		}
	}

	@Override
	public Coupon createCoupon(CouponDto dto) {
		Coupon coupon1 = new Coupon();
		if (dto.getCouponType().trim().equals(PRODUCT_WISE_COUPON)) {
			ProductWiseCoupon coupon = new ProductWiseCoupon();
			coupon.setCouponType(dto.getCouponType().trim());
			coupon.setProductId(dto.getProductId());
			coupon.setDiscount(dto.getDiscount());
			coupon1 = coupon;
		} else if (dto.getCouponType().trim().equals(CART_WISE_COUPON)) {
			CartWiseCoupon coupon = new CartWiseCoupon();
			coupon.setCouponType(dto.getCouponType().trim());
			coupon.setThreshold(100);
			coupon.setDiscount(20);
			coupon1 = coupon;
		} else if (dto.getCouponType().trim().equals(BXGY_COUPON)) {
			BXGY coupon = new BXGY();
			coupon.setCouponType(dto.getCouponType().trim());
			coupon.setBuyProducts(dto.getBuyProducts());
			coupon.setGetProducts(dto.getGetProducts());
			coupon.setRepetitionLimit(dto.getRepetitionLimit());
			coupon1 = coupon;
		}
		return repository.save(coupon1);
	}

	@Override
	public void deleteCoupon(long couponId) {
		Optional<Coupon> couponEntity = repository.findById(couponId);
		if (couponEntity.isPresent()) {
			repository.deleteById(couponId);
		} else {
			throw new CouponNotFoundException(STRING_MESSAGE + couponId);
		}
	}

	@Override
	public List<ApplicableCouponResponseDto> getApplicableCoupon(CartDto cartDto) {
		List<ApplicableCouponResponseDto> list = new ArrayList<>();

		// Calculate total price of all items in the cart
		double totalPrice = cartDto.getItem().stream().mapToDouble(Item::getPrice).sum();

		// Fetch all coupons at once
		List<Coupon> couponList = this.getAllCoupons();

		// Cart-wise Coupons (Cart total > 100)
		List<CartWiseCoupon> cartwiseList = couponList.stream()
				.filter(coupon -> CART_WISE_COUPON.equals(coupon.getCouponType())).map(CartWiseCoupon.class::cast)
				.toList();

		cartwiseList.forEach(cart -> {
			// Apply Cart-wise coupon if totalPrice > threshold (100)
			if (totalPrice > 100) {
				// Apply discount (e.g., if it's a percentage discount, apply it to totalPrice)
				double discount = totalPrice * (cart.getDiscount() / 100); // Assuming discount is percentage
				ApplicableCouponResponseDto dto = new ApplicableCouponResponseDto();
				dto.setCouponId(cart.getCouponId());
				dto.setCouponType(cart.getCouponType());
				dto.setDiscount(discount);
				list.add(dto);
			}
		});

		// Product-wise Coupons (Check if products in the cart match)
		List<ProductWiseCoupon> productwiseList = couponList.stream()
				.filter(coupon -> PRODUCT_WISE_COUPON.equals(coupon.getCouponType())).map(ProductWiseCoupon.class::cast)
				.toList();
		cartDto.getItem().forEach(item -> {
			productwiseList.forEach(productCoupon -> {
				// Check if the product in the cart matches the product in the coupon
				if (productCoupon.getProductId().equals(item.getProductId())) {
					// Apply discount (e.g., assuming the discount is percentage-based)
					double discount = item.getPrice() * (productCoupon.getDiscount() / 100);
					ApplicableCouponResponseDto dto = new ApplicableCouponResponseDto();
					dto.setCouponId(productCoupon.getCouponId());
					dto.setCouponType(productCoupon.getCouponType());
					dto.setDiscount(discount);
					list.add(dto);
				}
			});
		});

		List<BXGY> bxgyList = couponList.stream().filter(coupon -> BXGY_COUPON.equals(coupon.getCouponType()))
				.map(BXGY.class::cast).toList();
		bxgyList.forEach(bxgy -> {
			// Count how many items from the 'buy' array are in the cart
			long buyCount = cartDto.getItem().stream().filter(item -> bxgy.getBuyProducts().stream()
					.anyMatch(buyProduct -> buyProduct.getProductId() == (item.getProductId()))).count();

			// If there are enough items in the 'buy' array to apply the coupon
			if (buyCount >= bxgy.getBuyProducts().size()) {
				// Calculate how many times the coupon can be applied
				int applicableTimes = (int) (buyCount / bxgy.getBuyProducts().size());
				applicableTimes = Math.min(applicableTimes, bxgy.getRepetitionLimit());

				// Calculate how many free items we can provide
				int freeItemsCount = applicableTimes * bxgy.getGetProducts().size();
				List<Product> freeItems = new ArrayList<>();

				// Limit free items to the available items in the cart
				List<Product> availableGetProducts = cartDto.getItem().stream().filter(item -> bxgy.getGetProducts()
						.stream().anyMatch(getProduct -> getProduct.getProductId() == item.getProductId())).map(p -> {
							Product pro = new Product();
							pro.setProductId(p.getProductId());
							pro.setQuantity(p.getQuantity());
							return pro;
						}).toList();

				// Get as many free items as possible from the 'get' array
				for (int i = 0; i < Math.min(freeItemsCount, availableGetProducts.size()); i++) {
					freeItems.add(availableGetProducts.get(i));
				}

				double freeItemDiscount = cartDto.getItem().stream()
						.filter(product -> freeItems.stream()
								.anyMatch(productId -> productId.getProductId() == product.getProductId()))
						.mapToDouble(Item::getPrice).sum();
				// Add the BXGY coupon to the ApplicableCouponResponseDto list
				ApplicableCouponResponseDto dto = new ApplicableCouponResponseDto();
				dto.setCouponId(bxgy.getCouponId());
				dto.setCouponType(bxgy.getCouponType());
				dto.setDiscount(freeItemDiscount); // Add the free items to the response
				list.add(dto);
			}
		});

		return list;
	}

	@Override
	public UpdatedCart applyCoupon(long id, CartDto cartDto) {
		UpdatedCart updatedCart = new UpdatedCart();
		Optional<Coupon> couponEntity = repository.findById(id);
		if (couponEntity.isPresent()) {
			Coupon coupon = couponEntity.get();
			double totalPrice = cartDto.getItem().stream().mapToDouble(Item::getPrice).sum();
			updatedCart.setItem(cartDto.getItem());
			updatedCart.setTotalPrice(totalPrice);
			double discount = 0;
			if (coupon.getCouponType().equals(CART_WISE_COUPON)) {
				CartWiseCoupon cartWiseCoupon = (CartWiseCoupon) coupon;
				discount = totalPrice / cartWiseCoupon.getDiscount();
			} else if (coupon.getCouponType().equals(PRODUCT_WISE_COUPON)) {
				ProductWiseCoupon productwisecoupon = (ProductWiseCoupon) coupon;
				discount = calculateDiscountProductWise(cartDto, productwisecoupon);
			} else if (coupon.getCouponType().equals(BXGY_COUPON)) {
				BXGY bxgy = (BXGY) coupon;
				discount = calculateDiscountBXGY(cartDto, bxgy);
			}
			updatedCart.setTotalDiscount(discount);
			updatedCart.setFinalPrice(totalPrice - discount);
			return updatedCart;
		} else {
			throw new CouponNotFoundException(STRING_MESSAGE + id);
		}
	}

	private double calculateDiscountProductWise(CartDto cartDto, ProductWiseCoupon productwisecoupon) {
		List<Item> discountedItemList = cartDto.getItem().stream()
				.filter(item -> item.getProductId() == productwisecoupon.getProductId()).toList();
		return discountedItemList.stream().map(Item::getPrice).map(price -> price / productwisecoupon.getDiscount())
				.reduce((double) 0, (a, b) -> a + b);
	}

	private double calculateDiscountBXGY(CartDto cartDto, BXGY bxgyCoupon) {
		double discount = 0.0;

		// Get the products in the cart
		List<Item> cartItems = cartDto.getItem();

		// Count how many products from the "buy" array are in the cart
		long buyCount = cartItems.stream()
				.filter(cartItem -> bxgyCoupon.getBuyProducts().contains(cartItem.getProductId())).count();

		// If there aren't enough products from the "buy" array, no discount can be
		// applied
		if (buyCount < bxgyCoupon.getBuyProducts().size()) {
			return discount; // No discount, return 0
		}

		// Determine how many times the coupon can be applied (based on buyCount)
		int eligibleCouponUses = (int) (buyCount / bxgyCoupon.getBuyProducts().size());

		// Apply repetition limit
		eligibleCouponUses = Math.min(eligibleCouponUses, bxgyCoupon.getRepetitionLimit());

		// Calculate the number of free products
		int freeProductCount = eligibleCouponUses * bxgyCoupon.getGetProducts().size();

		// Get the list of products in the "get" array
		List<Product> getProducts = bxgyCoupon.getGetProducts();

		// Calculate the total discount by summing the prices of the free products
		List<Product> freeItems = getProducts.stream().limit(freeProductCount).toList();

		return cartDto.getItem().stream().filter(
				product -> freeItems.stream().anyMatch(productId -> productId.getProductId() == product.getProductId()))
				.mapToDouble(Item::getPrice).sum();
	}

}
