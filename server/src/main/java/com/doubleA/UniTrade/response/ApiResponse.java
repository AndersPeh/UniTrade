package com.doubleA.UniTrade.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//This class represents a generic API response structure.
// It contains a message and data field to encapsulate the response information.
// This function is for successful API responses, providing a consistent format
// for returning data to the client.
@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
}