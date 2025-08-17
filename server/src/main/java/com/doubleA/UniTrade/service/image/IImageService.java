package com.doubleA.UniTrade.service.image;

import com.doubleA.UniTrade.dtos.ImageDto;
import com.doubleA.UniTrade.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//Interface for ImageService

public interface IImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    void updateImage(MultipartFile file, Long imageId);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
}
