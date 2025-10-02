package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.request.AddProductRequest;
import com.doubleA.UniTrade.dtos.ProductDto;
import com.doubleA.UniTrade.request.ProductUpdateRequest;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.service.product.IProductService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Basically, this controller handles all the requests related to products.
// It maps the incoming HTTP requests to the appropriate service methods and returns the responses.
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final IProductService productService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  @GetMapping("product/{productId}/product")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
    Product product = productService.getProductById(productId);
    ProductDto productDto = productService.convertToDto(product);
    return ResponseEntity.ok(new ApiResponse("Success", productDto));
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
    Product theProduct = productService.addProduct(product);
    ProductDto productDto = productService.convertToDto(theProduct);
    return ResponseEntity.ok(new ApiResponse("Add Product Success", productDto));
  }

  @PutMapping("/product/{productId}/update")
  public ResponseEntity<ApiResponse> updateProduct(
      @RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
    Product theProduct = productService.updateProduct(request, productId);
    ProductDto productDto = productService.convertToDto(theProduct);
    return ResponseEntity.ok(new ApiResponse("Update Product Success", productDto));
  }

  @DeleteMapping("/product/{productId}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
    productService.deleteProductById(productId);
    return ResponseEntity.ok(new ApiResponse("Delete Product Success", productId));
  }

  @GetMapping("/products/by/brand-and-name")
  public ResponseEntity<ApiResponse> getProductsByBrandAndName(
      @RequestParam String brandName, @RequestParam String productName) {
    List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  @GetMapping("/products/by/category-and-brand")
  public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(
      @RequestParam String category, @RequestParam String brand) {
    List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  @GetMapping("/products/{name}/products")
  public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
    List<Product> products = productService.getProductsByName(name);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  //called method in ProductService to get products by brand
  //This is for sidebar filter in front end
  @GetMapping("/product/by-brand")
  public ResponseEntity<ApiResponse> findProductsByBrand(@RequestParam String brand) {
    List<Product> products = productService.getProductsByBrand(brand);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  @GetMapping("/product/{category}/all/products")
  public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category) {
    List<Product> products = productService.getProductsByCategory(category);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  @GetMapping("/distinct/products")
  public ResponseEntity<ApiResponse> getDistinctProductsByName() {
    List<Product> products = productService.findDistinctProductsByName();
    List<ProductDto> productDtos = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", productDtos));
  }

    @GetMapping("/distinct/brands")
    public ResponseEntity<ApiResponse> getDistinctBrands() {

        return ResponseEntity.ok(new ApiResponse("Success", productService.getAllDistinctBrands()));
    }
}
