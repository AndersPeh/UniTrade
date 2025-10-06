package com.doubleA.UniTrade.service.product;

import com.doubleA.UniTrade.model.Category;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.*;
import com.doubleA.UniTrade.request.AddProductRequest;
import com.doubleA.UniTrade.request.ProductUpdateRequest;
import com.doubleA.UniTrade.service.embeddings.ImageSearchService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for ProductService Demonstrates MOCK usage with Mockito framework
 *
 * <p>Test Coverage: 1. Add Product - Success case 2. Add Product - Validation failure (duplicate)
 * 3. Get Product By ID - Success case 4. Get Product By ID - Not found exception 5. Get All
 * Products - Success case
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  // MOCK - Simulated dependencies
  @Mock private ProductRepository productRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private CartItemRepository cartItemRepository;
  @Mock private OrderItemRepository orderItemRepository;
  @Mock private ImageRepository imageRepository;
  @Mock private ModelMapper modelMapper;
  @Mock private ImageSearchService imageSearchService;

  // System Under Test - ProductService with mocked dependencies injected
  @InjectMocks private ProductService productService;

  private Product testProduct;
  private Category testCategory;

  @BeforeEach
  void setUp() {
    testCategory = new Category("Clothing");
    testCategory.setId(1L);
    testCategory.setProducts(new ArrayList<>());

    testProduct =
        new Product(
            "White Hoodie",
            "Nike",
            new BigDecimal("49.99"),
            100,
            "Comfortable white hoodie",
            testCategory);
    testProduct.setId(1L);
  }

  /**
   * TEST 1: Add Product - Success Case Tests: ProductService.addProduct() method Verifies: Product
   * is created when it doesn't already exist
   */
  @Test
  void testAddProduct_Success() {
    // Arrange
    AddProductRequest request = new AddProductRequest();
    request.setName("White Hoodie");
    request.setBrand("Nike");
    request.setPrice(new BigDecimal("49.99"));
    request.setInventory(100);
    request.setDescription("Comfortable white hoodie");
    request.setCategory(testCategory);

    // Mock behavior: Product doesn't exist yet
    when(productRepository.existsByNameAndBrand("White Hoodie", "Nike")).thenReturn(false);
    when(productRepository.save(any(Product.class))).thenReturn(testProduct);

    // Act
    Product result = productService.addProduct(request);

    // Assert
    assertNotNull(result);
    assertEquals("White Hoodie", result.getName());
    assertEquals("Nike", result.getBrand());
    assertEquals(new BigDecimal("49.99"), result.getPrice());

    // Verify mock interactions
    verify(productRepository, times(1)).existsByNameAndBrand("White Hoodie", "Nike");
    verify(productRepository, times(1)).save(any(Product.class));
  }

  /**
   * TEST 2: Add Product - Duplicate Product Exception Tests: ProductService.addProduct() validation
   * Verifies: EntityExistsException is thrown when product already exists
   */
  @Test
  void testAddProduct_ThrowsEntityExistsException() {
    // Arrange
    AddProductRequest request = new AddProductRequest();
    request.setName("White Hoodie");
    request.setBrand("Nike");
    request.setCategory(testCategory);

    // Mock: Product already exists
    when(productRepository.existsByNameAndBrand("White Hoodie", "Nike")).thenReturn(true);

    // Act & Assert
    EntityExistsException exception =
        assertThrows(EntityExistsException.class, () -> productService.addProduct(request));

    // Verify exception message contains product details
    String message = exception.getMessage();
    assertTrue(
        message.contains("White Hoodie") || message.contains("Nike"),
        "Exception message should mention the product. Actual: " + message);

    // Verify save was never called
    verify(productRepository, never()).save(any(Product.class));
  }

  /**
   * TEST 3: Get Product By ID - Success Case Tests: ProductService.getProductById() method
   * Verifies: Product is retrieved when ID exists
   */
  @Test
  void testGetProductById_Success() {
    // Arrange
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

    // Act
    Product result = productService.getProductById(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("White Hoodie", result.getName());
    assertEquals("Nike", result.getBrand());

    verify(productRepository, times(1)).findById(1L);
  }

  /**
   * TEST 4: Get Product By ID - Not Found Exception Tests: ProductService.getProductById() error
   * handling Verifies: EntityNotFoundException is thrown when product doesn't exist
   */
  @Test
  void testGetProductById_ThrowsEntityNotFoundException() {
    // Arrange
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    EntityNotFoundException exception =
        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(999L));

    // Verify exception is thrown (message check optional)
    assertNotNull(exception);

    verify(productRepository, times(1)).findById(999L);
  }

  /**
   * TEST 5: Get All Products - Success Case Tests: ProductService.getAllProducts() method Verifies:
   * All products are retrieved from repository
   */
  @Test
  void testGetAllProducts_Success() {
    // Arrange
    Product product2 =
        new Product(
            "Black Hoodie", "Adidas", new BigDecimal("59.99"), 50, "Black hoodie", testCategory);
    product2.setId(2L);

    List<Product> products = Arrays.asList(testProduct, product2);
    when(productRepository.findAll()).thenReturn(products);

    // Act
    List<Product> result = productService.getAllProducts();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("White Hoodie", result.get(0).getName());
    assertEquals("Black Hoodie", result.get(1).getName());

    verify(productRepository, times(1)).findAll();
  }
}
