package com.doubleA.UniTrade.response;

import lombok.AllArgsConstructor;
import lombok.Data;


// This class represents an error response structure for the API.
// It contains a message field to provide details about the error that occurred.
// This is used to standardize error responses across the application, making it easier for clients to
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}