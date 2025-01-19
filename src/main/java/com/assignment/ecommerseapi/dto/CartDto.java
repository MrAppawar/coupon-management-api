package com.assignment.ecommerseapi.dto;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.assignment.ecommerseapi.model.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CartDto {

	private List<Item> item;
}
