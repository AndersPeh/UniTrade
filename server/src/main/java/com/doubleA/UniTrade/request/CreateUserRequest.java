package com.doubleA.UniTrade.request;

import com.doubleA.UniTrade.model.Address;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class CreateUserRequest {
  private String firstName;
  private String lastName;

  private String email;
  private String password;

  private List<Address> addressList;
}
