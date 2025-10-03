package com.doubleA.UniTrade.security.user;

import com.doubleA.UniTrade.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetails implements UserDetails {

  private Long id;
  private String email;
  private String password;

  private Collection<GrantedAuthority> authorities;

  // This method iterates through roles of the user, maps them into
  // a list of GrantedAuthority.
  // Because Spring Security only understands roles in GrantedAuthority.
  public static ShopUserDetails buildUserDetails(User user) {
    List<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    // returns new ShopUserDetails instance as it can get ShopUserDetails's fields from
    // user argument.
    return new ShopUserDetails(user.getId(), user.getEmail(), user.getPassword(), authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }
}
