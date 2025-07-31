package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// This repository allows ProductService to interact with the database.
// JpaRepository<Product, Long> allows ProductRepository to carry out database operations
// for Product entities, using Long as the primary key type.
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByCategoryName(String category);

    List<Product> findByBrandAndName(String brand, String name);

    List<Product> findByBrand(String brand);

    List<Product> findByName(String name);
}
