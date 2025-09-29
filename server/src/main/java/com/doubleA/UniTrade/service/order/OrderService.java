package com.doubleA.UniTrade.service.order;

import com.doubleA.UniTrade.dtos.OrderDto;
import com.doubleA.UniTrade.enums.OrderStatus;
import com.doubleA.UniTrade.model.Cart;
import com.doubleA.UniTrade.model.Order;
import com.doubleA.UniTrade.model.OrderItem;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.OrderRepository;
import com.doubleA.UniTrade.repository.ProductRepository;
import com.doubleA.UniTrade.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final ICartService cartService;
  private final ModelMapper modelMapper;

  @Override
  public Order placeOrder(Long userId) {
    // When user places an order, get the cart of the user.
    Cart cart = cartService.getCartByUserId(userId);
    // use the user in the cart to set user of the order.
    // create order also sets order status and date.
    Order order = createOrder(cart);
    // use the user's cart to iterate the cart items, get product of each cart item, subtract the
    // inventory of each product by cart item quantity
    // to update the product inventory for products sold.
    // use cart item quantity and unit price to set order item quantity and price
    // use product and order to set order item reference to product and order.
    List<OrderItem> orderItemList = createOrderItems(order, cart);
    // use the order item list created as order items in order
    order.setOrderItems(new HashSet<>(orderItemList));
    // order needs total amount, iterate through orderItemList to reduce the total price.
    order.setTotalAmount(calculateTotalAmount(orderItemList));

    Order savedOrder = orderRepository.save(order);
    cartService.clearCart(cart.getId());
    return savedOrder;
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();
    order.setUser(cart.getUser());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setOrderDate(LocalDate.now());

    return order;
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getItems().stream()
        .map(
            cartItem -> {
              Product product = cartItem.getProduct();
              product.setInventory(product.getInventory() - cartItem.getQuantity());
              productRepository.save(product);
              return new OrderItem(cartItem.getQuantity(), cartItem.getUnitPrice(), product, order);
            })
        .toList();
  }

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
    return orderItemList.stream()
        .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public List<OrderDto> getUserOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);

    return orders.stream().map(this::convertToDto).toList();
  }

  @Override
  public OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }
}
