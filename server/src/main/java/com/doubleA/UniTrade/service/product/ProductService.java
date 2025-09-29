package com.doubleA.UniTrade.service.product;

import com.doubleA.UniTrade.model.*;
import com.doubleA.UniTrade.dtos.ImageDto;
import com.doubleA.UniTrade.dtos.ProductDto;
import com.doubleA.UniTrade.repository.*;
import com.doubleA.UniTrade.request.AddProductRequest;
import com.doubleA.UniTrade.request.ProductUpdateRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// this marks ProductService as a service bean so it will be used as
// concrete instance of IProductService in dependency injection.
@Service

// It automatically generates a constructor and store properties marked with final or nonnull
// as fields and parameters (to be assigned value through dependency injection).
// In that constructor, it injects ProductRepository (it is marked as spring bean)
// and assign it to productRepository of ProductService so ProductService can interact with the
// database.
// For example,
// public ProductService(ProductRepository productRepository) {
//    this.productRepository = productRepository;}
@RequiredArgsConstructor

// implements IProductService means ProductService provides concrete implementation
// for methods defined in IProductService interface.
// When any class uses dependency injection on IProductService, Spring automatically
// provides an instance of ProductService so any class injecting IProductService is dependent
// on the interface but using the logic of ProductService.
// So the class injecting the interface is loosely coupled with IProductService, any changes on
// ProductService won't
// affect the class injecting the interface because the interface is static. The class is dependent
// on the interface,
// not the concrete implementing class.
public class ProductService implements IProductService {

  // Marking it as final ensures it must be assigned exactly once through the constructor and cannot
  // be changed later.
  // It inherits from JpaRepository, so it will be added to the constructor parameter
  // for dependency injection to be assigned value.
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderItemRepository orderItemRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  // Change from receive product repository to receive AddProductRequest since we want to check if
  // the new product.
  // already exist in the database before adding it.
  // The change also extend to addProduct method in iProductService interface.
  // It will also check whether the category already exists in the database or not.
  // Optional.ofNullable is used to handle the case where the category might not be found in the
  // database.
  // If it does not exist, it will create a new category (through orElseGet()) and save it to the
  // database
  // before set category in product.
  // If the category already exists, it will use the existing category from the database.
  @Override
  public Product addProduct(AddProductRequest request) {
    if (productExists(request.getName(), request.getBrand())) {
      throw new EntityExistsException(
          request.getName() + " already exists. Please choose a different name or brand.");
    }
    Category category =
        Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
            .orElseGet(
                () -> {
                  ;
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
        category);
  }

  // updateProduct define the logic to update an existing product.
  // It first checks if the product with the given productId exists in the database.
  // If it exists, it updates the existing product with the new values from ProductUpdateRequest
  // using updateExistingProduct method.
  // It then saves the updated product back to the database using productRepository.save().
  @Override
  public Product updateProduct(ProductUpdateRequest request, Long productId) {
    return productRepository
        .findById(productId)
        .map(existingProduct -> updateExistingProduct(existingProduct, request))
        .map(productRepository::save)
        .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
  }

  // It updates the fields of the existing Product with the values receive from the
  // ProductUpdateRequest.
  // It also retrieves the Category by name from the categoryRepository and sets it to the existing
  // Product.
  // Finally, it returns the updated Product object.
  // will update comment again later
  private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
    existingProduct.setName(request.getName());
    existingProduct.setBrand(request.getBrand());
    existingProduct.setPrice(request.getPrice());
    existingProduct.setInventory(request.getInventory());
    existingProduct.setDescription(request.getDescription());
    // Category category = categoryRepository.findByName(request.getCategory().getName());

    Category category =
        Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
            .orElseGet(
                () -> {
                  ;
                  Category newCategory = new Category(request.getCategory().getName());
                  return categoryRepository.save(newCategory);
                });

    existingProduct.setCategory(category);
    return existingProduct;
  }

  // deleteProductById defines the logic to delete a product by its ID.
  // It first checks if the product with the given productId exists in the database.
  // If it exists, it retrieves the associated CartItems and OrderItems.
  // For each CartItem, it removes the CartItem from the Cart and deletes the CartItem from the
  // database.
  // For each OrderItem, it sets the product to null and saves the OrderItem back to the database.
  // It then removes the product from its associated Category (if it exists) and sets the product's
  // category to null.
  // Finally, it deletes the product from the database.
  // will update comment again later
  @Override
  public void deleteProductById(Long productId) {
    productRepository
        .findById(productId)
        .ifPresentOrElse(
            product -> {

              // As there is no point to keep cart item for future reference (cant place order
              // anyway),
              // remove the cart item entirely.
              List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
              cartItems.forEach(
                  cartItem -> {
                    Cart cart = cartItem.getCart();
                    cart.removeItem(cartItem);

                    // As these cartItem are not associated to any cart, orphanRemoval = true will
                    // delete these cartItem.
                    // cartItemRepository.delete(cartItem);
                  });

              // Instead of removing order item from order, we only set the product in the order
              // item to be null.
              // Because we still need the order item to refer to our purchase history.
              List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
              orderItems.forEach(
                  orderItem -> {
                    orderItem.setProduct(null);
                    orderItemRepository.save(orderItem);
                  });

              Optional.ofNullable(product.getCategory())
                  .ifPresent(category -> category.getProducts().remove(product));

              product.setCategory(null);
              productRepository.deleteById(productId);
            },
            // Empty action of  ifPresentOrElse.
            () -> {
              ;
              throw new EntityNotFoundException("Product not found.");
            });
  }

  @Override
  public Product getProductById(Long productId) {
    return productRepository
        .findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("Product Not Found."));
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
  public List<Product> findDistinctProductsByName() {
    List<Product> products = getAllProducts();
    Map<String, Product> distinctProductMap =
        products.stream()
            .collect(
                Collectors.toMap(
                    Product::getName, product -> product, (existing, replacement) -> existing));
    return new ArrayList<>(distinctProductMap.values());
  }

  @Override
  public List<Product> getProductsByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public List<ProductDto> getConvertedProducts(List<Product> products) {
    return products.stream().map(this::convertToDto).toList();
  }

  // converts a Product object to a ProductDto object using ModelMapper.
  @Override
  public ProductDto convertToDto(Product product) {
    ProductDto productDto = modelMapper.map(product, ProductDto.class);
    List<Image> images = imageRepository.findByProductId(product.getId());
    List<ImageDto> imageDtos =
        images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
    productDto.setImages(imageDtos);
    return productDto;
  }
}
