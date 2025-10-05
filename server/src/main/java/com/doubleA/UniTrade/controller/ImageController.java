package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.dtos.ImageDto;
import com.doubleA.UniTrade.model.Image;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// Basically, this controller handles all the requests related to products image.
// It maps the incoming HTTP requests to the appropriate service methods and returns the responses.
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {

  private final IImageService imageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> uploadImages(
      @RequestParam("files") List<MultipartFile> files, @RequestParam("productId") Long productId) {
    List<ImageDto> imageDto = imageService.saveImages(productId, files);
    return ResponseEntity.ok(new ApiResponse("Images Upload Success", imageDto));
  }

  @GetMapping("/image/download/{imageId}")
  public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
    Image image = imageService.getImageById(imageId);
    ByteArrayResource resource =
        new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(image.getFileType()))
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
        .body(resource);
  }

  @PutMapping("/image/{imageId}/update")
  public ResponseEntity<ApiResponse> updateImage(
      @PathVariable Long imageId, @RequestBody MultipartFile file, @RequestParam Long productId)
      throws IOException {
    imageService.updateImage(file, imageId, productId);
    return ResponseEntity.ok(new ApiResponse("Image Update Success", null));
  }

  @DeleteMapping("/image/{imageId}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
    imageService.deleteImageById(imageId);
    return ResponseEntity.ok(new ApiResponse("Image Deletion Success!", null));
  }
}
