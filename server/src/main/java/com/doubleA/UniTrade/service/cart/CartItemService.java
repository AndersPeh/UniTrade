package com.doubleA.UniTrade.service.cart;

import com.doubleA.UniTrade.dtos.CartItemDto;
import com.doubleA.UniTrade.model.Cart;
import com.doubleA.UniTrade.model.CartItem;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.CartItemRepository;
import com.doubleA.UniTrade.repository.CartRepository;
import com.doubleA.UniTrade.service.product.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
  private final CartItemRepository cartItemRepository;
  private final CartRepository cartRepository;
  private final ICartService cartService;
  private final IProductService productService;
  private final ModelMapper modelMapper;

  @Override
  public CartItem addItemToCart(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getCart(cartId);

    Product product = productService.getProductById(productId);

    CartItem cartItem =
        // Get items from the cart, iterate through the items
        cart.getItems().stream()
            // find item where the product id of the item matches the request product id
            .filter(item -> item.getProduct().getId().equals(productId))
            // That means the cart item that the user wants to add already exists.
            .findFirst()
            // If the product that the user wants to add doesnt exist in the cart, create a new cart
            // item.
            .orElse(new CartItem());

    // It is only null when orElse(new CartItem()) condition is met, meaning the user adds a new
    // item.
    if (cartItem.getId() == null) {
      cartItem.setCart(cart);
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setUnitPrice(product.getPrice());

      // If the cart item of the same product already exists
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }
    cartItem.setTotalPrice();
    cart.addItem(cartItem);
    cartItemRepository.save(cartItem);
    cartRepository.save(cart);
    return  cartItem;
  }

  @Override
  public void removeItemFromCart(Long cartId, Long productId) {
    Cart cart = cartService.getCart(cartId);

    CartItem cartItemToRemove = getCartItem(cartId, productId);

    cart.removeItem(cartItemToRemove);
    cartRepository.save(cart);
  }

  @Override
  public void updateItemQuantity(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getCart(cartId);

    cart.getItems().stream()
        // find item where the product id of the item matches the request product id
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .ifPresent(
            item -> {
              item.setQuantity(quantity);
              // item.setUnitPrice(item.getProduct().getPrice());
              item.setTotalPrice();
            });

    BigDecimal totalAmount =
        // Iterates through cartItems in the cart
        cart.getItems().stream()
            // For every cartItem, only get the total price
            .map(CartItem::getTotalPrice)
            // For every total price (starting from ZERO), sum them up.
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    cart.setTotalAmount(totalAmount);
    cartRepository.save(cart);
  }

  @Override
  public CartItem getCartItem(Long cartId, Long productId) {
    Cart cart = cartService.getCart(cartId);

    return
    // Get items from the cart, iterate through the items
    cart.getItems().stream()
        // find item where the product id of the item matches the request product id
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        // If the product that the user wants to add doesnt exist in the cart
        .orElseThrow(() -> new EntityNotFoundException("Cart Not Found"));
  }
    @Override
    public CartItemDto convertToDto(CartItem cartItem){
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
