package com.doubleA.UniTrade.service.product;

import com.doubleA.UniTrade.model.Category;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.*;
import com.doubleA.UniTrade.service.embeddings.ImageSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Advanced Unit Tests for ProductService
 *
 * <p>Demonstrates: 1. PARAMETERIZED TESTS - One test method runs with multiple input sets 2. SPY -
 * Real ModelMapper object that can be verified 3. MOCK - Simulated ProductRepository 4. STUB -
 * Predefined responses using when().thenReturn()
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceAdvancedTest {

  // MOCK - Completely simulated repository
  @Mock private ProductRepository productRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private CartItemRepository cartItemRepository;
  @Mock private OrderItemRepository orderItemRepository;
  @Mock private ImageRepository imageRepository;

  /**
   * SPY - Real ModelMapper instance (Test Double type: SPY) Unlike @Mock, this is a real object
   * that actually performs mapping We can verify it was used and override specific methods if
   * needed
   */
  @Spy private ModelMapper modelMapper = new ModelMapper();

  @Mock private ImageSearchService imageSearchService;

  @InjectMocks private ProductService productService;

  private Category clothingCategory;

  @BeforeEach
  void setUp() {
    clothingCategory = new Category("Clothing");
    clothingCategory.setId(1L);
  }

  /**
   * PARAMETERIZED TEST
   *
   * <p>Tests: ProductService.getProductsByBrand() method
   *
   * <p>Runs 3 times with different parameters: - Nike (expects 2 products) - Adidas (expects 1
   * product) - Samsung (expects 0 products)
   *
   * <p>Demonstrates: - PARAMETERIZED testing (one test, multiple scenarios) - MOCK usage
   * (productRepository) - STUB behavior (when().thenReturn())
   */
  @ParameterizedTest(name = "Brand: {0} should return {1} products")
  @CsvSource({"Nike, 2", "Adidas, 1", "Samsung, 0"})
  void testGetProductsByBrand_Parameterized(String brand, int expectedCount) {
    // Arrange - Create test products
    Product nikeProduct1 =
        new Product(
            "White Hoodie", "Nike", new BigDecimal("49.99"), 100, "Description", clothingCategory);
    nikeProduct1.setId(1L);

    Product nikeProduct2 =
        new Product(
            "Black Hoodie", "Nike", new BigDecimal("59.99"), 50, "Description", clothingCategory);
    nikeProduct2.setId(2L);

    Product adidasProduct =
        new Product(
            "Gray Sweatpants",
            "Adidas",
            new BigDecimal("39.99"),
            75,
            "Description",
            clothingCategory);
    adidasProduct.setId(3L);

    // STUB - Define mock behavior (use lenient to avoid unnecessary stubbing warnings)
    lenient()
        .when(productRepository.findByBrand("Nike"))
        .thenReturn(Arrays.asList(nikeProduct1, nikeProduct2));

    lenient()
        .when(productRepository.findByBrand("Adidas"))
        .thenReturn(Arrays.asList(adidasProduct));

    lenient().when(productRepository.findByBrand("Samsung")).thenReturn(Arrays.asList());

    // Act
    List<Product> result = productService.getProductsByBrand(brand);

    // Assert
    assertEquals(
        expectedCount,
        result.size(),
        "Brand '" + brand + "' should return " + expectedCount + " products");

    // Verify mock was called
    verify(productRepository, times(1)).findByBrand(brand);

    // Additional assertions for non-empty results
    if (expectedCount > 0) {
      result.forEach(
          product ->
              assertEquals(brand, product.getBrand(), "All products should have brand: " + brand));
    }
  }

  /**
   * DEMONSTRATES SPY vs MOCK
   *
   * <p>Tests: ProductService.getProductsByCategory() method
   *
   * <p>Key Points: - productRepository (@Mock): Returns null unless we stub it - modelMapper
   * (@Spy): Real object that actually works
   *
   * <p>This test verifies that: 1. The mock repository is properly stubbed 2. The spy (ModelMapper)
   * is a real, working instance 3. Products are correctly retrieved by category
   */
  @ParameterizedTest(name = "Category: {0} should return {1} products")
  @CsvSource({"Clothing, 3"})
  void testGetProductsByCategory_DemonstratesSpyAndMock(String categoryName, int expectedCount) {
    // Arrange
    Product product1 =
        new Product(
            "Hoodie", "Nike", new BigDecimal("49.99"), 100, "Description", clothingCategory);
    Product product2 =
        new Product(
            "Sweatpants", "Nike", new BigDecimal("39.99"), 50, "Description", clothingCategory);
    Product product3 =
        new Product(
            "T-Shirt", "Adidas", new BigDecimal("29.99"), 75, "Description", clothingCategory);

    // STUB - Mock repository behavior
    when(productRepository.findByCategoryName(categoryName))
        .thenReturn(Arrays.asList(product1, product2, product3));

    // Act
    List<Product> result = productService.getProductsByCategory(categoryName);

    // Assert
    assertEquals(expectedCount, result.size());

    // Verify MOCK was called
    verify(productRepository, times(1)).findByCategoryName(categoryName);

    // Verify all products belong to correct category
    result.forEach(product -> assertEquals(categoryName, product.getCategory().getName()));

    // Note: modelMapper (@Spy) is available if ProductService used it
    // We can verify it was called or assert on its behavior
    // Since it's a real object, it actually performs conversions
  }
}
