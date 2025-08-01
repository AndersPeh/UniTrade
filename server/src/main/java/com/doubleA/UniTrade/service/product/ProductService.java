package com.doubleA.UniTrade.service.product;

import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// this marks ProductService as a service bean so it will be used as
// concrete instance of IProductService in dependency injection.
@Service

// It automatically generates a constructor and store properties marked with final or nonnull
// as fields and parameters (to be assigned value through dependency injection).
// In that constructor, it injects ProductRepository (it is marked as spring bean)
// and assign it to productRepository of ProductService so ProductService can interact with the database.
// For example,
// public ProductService(ProductRepository productRepository) {
//    this.productRepository = productRepository;}
@RequiredArgsConstructor

// implements IProductService means ProductService provides concrete implementation
// for methods defined in IProductService interface.
// When any class uses dependency injection on IProductService, Spring automatically
// provides an instance of ProductService so any class injecting IProductService is dependent
// on the interface but using the logic of ProductService.
// So the class injecting the interface is loosely coupled with IProductService, any changes on ProductService won't
// affect the class injecting the interface because the interface is static. The class is dependent on the interface,
// not the concrete implementing class.
public class ProductService implements IProductService{

// Marking it as final ensures it must be assigned exactly once through the constructor and cannot be changed later.
// It inherits from JpaRepository, so it will be added to the constructor parameter
// for dependency injection to be assigned value.
    private final ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        return null;
    }

    @Override
    public void deleteProductById(Long productId) {

    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product Not Found."));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }
}
