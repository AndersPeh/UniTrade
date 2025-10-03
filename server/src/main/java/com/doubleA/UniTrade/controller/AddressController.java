package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.dtos.AddressDto;
import com.doubleA.UniTrade.model.Address;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.address.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/addresses")
public class AddressController {
  private final IAddressService addressService;

  @GetMapping("/{userId}/address")
  public ResponseEntity<ApiResponse> getUserAddresses(@PathVariable Long userId) {

    List<Address> addresses = addressService.getUserAddresses(userId);
    List<AddressDto> addressDtoList = addressService.convertToDto(addresses);
    return ResponseEntity.ok(new ApiResponse("Success", addressDtoList));
  }

  @GetMapping("/{addressId}/address")
  public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long addressId) {

    Address address = addressService.getAddressById(addressId);
    AddressDto addressDto = addressService.convertToDto(address);
    return ResponseEntity.ok(new ApiResponse("Success", addressDto));
  }

  @PostMapping("/{userId}/new")
  public ResponseEntity<ApiResponse> createAddresses(
      @RequestBody List<Address> addressList, @PathVariable Long userId) {

    List<Address> addresses = addressService.createAddress(addressList, userId);
    List<AddressDto> addressDtoList = addressService.convertToDto(addresses);
    return ResponseEntity.ok(new ApiResponse("Successfully Created Addresses", addressDtoList));
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long addressId) {
    addressService.deleteAddress(addressId);

    return ResponseEntity.ok(new ApiResponse("Successful Address Deletion", addressId));
  }

  @PutMapping("/{addressId}/update")
  public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long addressId, Address address) {

    Address addressUpdated = addressService.updateAddress(addressId, address);
    AddressDto addressDto = addressService.convertToDto(addressUpdated);
    return ResponseEntity.ok(new ApiResponse("Successfully Updated Address", addressDto));
  }
}
