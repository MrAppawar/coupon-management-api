package com.assignment.ecommerseapi.model;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue("bxgy")
public class BXGY extends Coupon {

	@ElementCollection
	@CollectionTable(name = "buy_products", joinColumns = @JoinColumn(name = "coupon_id"))
	@Column(name = "buyProducts")
	private List<Product> buyProducts;

	@ElementCollection
	@CollectionTable(name = "get_products", joinColumns = @JoinColumn(name = "coupon_id"))
	@Column(name = "getProducts")
	private List<Product> getProducts;

	private int repetitionLimit;
}
