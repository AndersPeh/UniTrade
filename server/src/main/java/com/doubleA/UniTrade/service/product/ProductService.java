package com.doubleA.UniTrade.service.product;

import com.doubleA.UniTrade.model.Category;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.ProductRepository;
import com.doubleA.UniTrade.repository.CategoryRepository;
import com.doubleA.UniTrade.request.AddProductRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private final CategoryRepository categoryRepository;

// Change from receive product repository to receive AddProductRequest since we want to check if the new product.
// already exist in the database before adding it.
// The change also extend to addProduct method in iProductService interface.
// It will also check whether the category already exists in the database or not.
// Optional.ofNullable is used to handle the case where the category might not be found in the database.
// If it does not exist, it will create a new category (through orElseGet()) and save it to the database
// before set category in product.
// If the category already exists, it will use the existing category from the database.
    @Override
    public Product addProduct(AddProductRequest request) {
        if (productExists(request.getName(), request.getBrand())){
            throw new EntityExistsException(request.getName() + "already exists in the database.");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {;
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
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
