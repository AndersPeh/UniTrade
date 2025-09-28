package com.doubleA.UniTrade.request;

import com.doubleA.UniTrade.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

// This class handle data that received from the client when adding a new product.
// It contains fields that match the Product entity, so it can be used to create a new Product object.
// The fields are annotated with @Data from Lombok to generate boilerplate code like getters and setters.
@Data
public class AddProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer inventory;
    private String description;
    private Category category;
}
