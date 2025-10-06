package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.request.AddProductRequest;
import com.doubleA.UniTrade.dtos.ProductDto;
import com.doubleA.UniTrade.request.ProductUpdateRequest;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// Basically, this controller handles all the requests related to products.
// It maps the incoming HTTP requests to the appropriate service methods and returns the responses.
@RestController
@RequiredArgsConstructor
@Slf4j
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
    Product theProduct = productService.addProduct(product);
    ProductDto productDto = productService.convertToDto(theProduct);
    return ResponseEntity.ok(new ApiResponse("Add Product Success", productDto));
  }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/product/{productId}/update")
  public ResponseEntity<ApiResponse> updateProduct(
      @RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
    Product theProduct = productService.updateProduct(request, productId);
    ProductDto productDto = productService.convertToDto(theProduct);
    return ResponseEntity.ok(new ApiResponse("Update Product Success", productDto));
  }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

  // called method in ProductService to get products by brand
  // This is for sidebar filter in front end
  @GetMapping("/product/by-brand")
  public ResponseEntity<ApiResponse> findProductsByBrand(@RequestParam String brand) {
    List<Product> products = productService.getProductsByBrand(brand);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  @GetMapping("/{category}/products")
  public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category) {
    List<Product> products = productService.getProductsByCategory(category);
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
  }

  // This is for category section in footer
  @GetMapping("/category/{categoryId}/products")
  public ResponseEntity<ApiResponse> findProductsByCategoryId(@PathVariable Long categoryId) {
    List<Product> products = productService.getProductsByCategoryId(categoryId);
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

  @PostMapping("/search-by-image")
  public ResponseEntity<ApiResponse> searchByImage(@RequestParam("image") MultipartFile image)
      throws IOException {
    List<Product> products = productService.searchProductsByImage(image);
    log.info("Found : {} ", products.size() + " products");
    List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
    log.info("Found products dto : {} ", products);
    String message = "Search performed with " + convertedProducts.size() + " results.";

    return ResponseEntity.ok(new ApiResponse(message, convertedProducts));
  }
}
