package com.doubleA.UniTrade.dtos;
import com.doubleA.UniTrade.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// This class represents a Data Transfer Object (DTO) for product.
// It contains fields for the product ID, product name, product brand, product price, etc.
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<ImageDto> images;
}