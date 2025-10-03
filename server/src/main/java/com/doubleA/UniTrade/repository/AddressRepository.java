package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUserId(Long userId);
}
