package com.doubleA.UniTrade.service.product;

import com.doubleA.UniTrade.model.Product;

import java.util.List;

// Interface is for defining contract (like a rule) for product-related operations.
// When it is injected later on, any class dependent it will have access to the logic defined in ProductService.
public interface IProductService {

// need to specify what these methods take as parameters and return for implementing class (ProductService)
// to know what to expect.
// addProduct, updateProduct and getProductById return Product object.
    Product addProduct(Product product);
    Product updateProduct(Product product, Long productId);
    Product getProductById(Long productId);

// deleteProductById has nothing to return.
    void deleteProductById(Long productId);

// return List of Product when we get all products.
    List<Product> getAllProducts();

    List<Product>getProductsByCategory(String category);

    List<Product>getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product>getProductsByName(String name);

    List<Product>getProductsByBrandAndName(String brand, String name);

}






















