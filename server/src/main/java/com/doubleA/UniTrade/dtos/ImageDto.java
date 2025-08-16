package com.doubleA.UniTrade.dtos;

import lombok.Data;

// This class represents a Data Transfer Object (DTO) for images.
// It contains fields for the image ID, file name, and download URL.
@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String downloadUrl;
}
