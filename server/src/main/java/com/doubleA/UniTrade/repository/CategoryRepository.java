package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


// This repository allows ProductService to check available category with the database.
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}