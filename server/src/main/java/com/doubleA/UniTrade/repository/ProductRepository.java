package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// This repository allows ProductService to interact with the database.
// JpaRepository<Product, Long> allows ProductRepository to carry out database operations
// for Product entities, using Long as the primary key type.
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByCategoryName(String category);

    List<Product> findByBrandAndName(String brand, String name);

    List<Product> findByBrand(String brand);

// When user types anything, it will be converted to lowercase name. Then it will be compared to lowercase name of
// products. As long as the user enters name that matches any part of the product name, results will be returned.
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);


    boolean existsByNameAndBrand(String name, String brand);
}
