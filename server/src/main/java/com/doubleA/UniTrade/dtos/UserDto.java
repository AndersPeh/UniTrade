package com.doubleA.UniTrade.dtos;

import com.doubleA.UniTrade.model.Order;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class UserDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private List<OrderDto> orders;
  private CartDto cart;
}
