package com.doubleA.UniTrade.service.image;

import com.doubleA.UniTrade.dtos.ImageDto;
import com.doubleA.UniTrade.model.Image;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// Interface for ImageService

public interface IImageService {
  Image getImageById(Long imageId);

  void deleteImageById(Long imageId);

  @Transactional
  void updateImage(MultipartFile file, Long imageId, Long productId) throws IOException;

  List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
}
