package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// This repository allows ProductService to interact with the database.
// JpaRepository<Product, Long> allows ProductRepository to carry out database operations
// for Product entities, using Long as the primary key type.
public interface ProductRepository extends JpaRepository<Product, Long> {

    // JPA uses a method naming convention to automatically generate queries by
    // following this pattern [prefix][Subject: optional]By[Predicate].
    // For example, findByCategoryNameAndBrand.
    // findBy will find Product entities (based on ProductRepository).
    // And logical operator to combine conditions.
    // CategoryName: filter by the name property of the associated Category entity.
    // Brand filter by the brand property of Product entity.

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByCategoryName(String category);

    List<Product> findByBrandAndName(String brand, String name);

    List<Product> findByBrand(String brand);

// When user types anything, it will be converted to lowercase name. Then it will be compared to lowercase name of
// products. As long as the user enters name that matches any part of the product name, results will be returned.
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);


    boolean existsByNameAndBrand(String name, String brand);

    List<Product> findAllByCategoryId(Long categoryId);
}
