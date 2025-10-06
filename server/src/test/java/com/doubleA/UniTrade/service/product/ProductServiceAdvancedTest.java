package com.doubleA.UniTrade.service.product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.doubleA.UniTrade.model.Category;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.CartItemRepository;
import com.doubleA.UniTrade.repository.CategoryRepository;
import com.doubleA.UniTrade.repository.ImageRepository;
import com.doubleA.UniTrade.repository.OrderItemRepository;
import com.doubleA.UniTrade.repository.ProductRepository;
import com.doubleA.UniTrade.service.embeddings.ImageSearchService;


@ExtendWith(MockitoExtension.class)
class ProductServiceAdvancedTest {


  @Mock private ProductRepository productRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private CartItemRepository cartItemRepository;
  @Mock private OrderItemRepository orderItemRepository;
  @Mock private ImageRepository imageRepository;


  @Spy private ModelMapper modelMapper = new ModelMapper();

  @Mock private ImageSearchService imageSearchService;

  @InjectMocks private ProductService productService;

  private Category clothingCategory;

  @BeforeEach
  void setUp() {
    clothingCategory = new Category("Clothing");
    clothingCategory.setId(1L);
  }


  @ParameterizedTest(name = "Brand: {0} should return {1} products")
  @CsvSource({"Nike, 2", "Adidas, 1", "Samsung, 0"})
  void testGetProductsByBrand_Parameterized(String brand, int expectedCount) {
    // Arrange
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


    verify(productRepository, times(1)).findByBrand(brand);


    if (expectedCount > 0) {
      result.forEach(
          product ->
              assertEquals(brand, product.getBrand(), "All products should have brand: " + brand));
    }
  }


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


    when(productRepository.findByCategoryName(categoryName))
        .thenReturn(Arrays.asList(product1, product2, product3));

    // Act
    List<Product> result = productService.getProductsByCategory(categoryName);

    // Assert
    assertEquals(expectedCount, result.size());


    verify(productRepository, times(1)).findByCategoryName(categoryName);


    result.forEach(product -> assertEquals(categoryName, product.getCategory().getName()));


  }
}
