package com.doubleA.UniTrade.service.image;

import com.doubleA.UniTrade.dtos.ImageDto;
import com.doubleA.UniTrade.model.Image;
import com.doubleA.UniTrade.model.Product;
import com.doubleA.UniTrade.repository.ImageRepository;
import com.doubleA.UniTrade.request.EmbeddingsDeleteRequest;
import com.doubleA.UniTrade.service.chroma.ChromaService;
import com.doubleA.UniTrade.service.embeddings.ImageSearchService;
import com.doubleA.UniTrade.service.product.IProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService implements IImageService {
  private final ImageRepository imageRepository;
  private final IProductService productService;
  private final ImageSearchService imageSearchService;
  private final ChromaService chromaService;

  @Value("${spring.ai.vectorstore.chroma.collection-name}")
  private String collectionName;

  @Override
  public Image getImageById(Long imageId) {
    return imageRepository
        .findById(imageId)
        .orElseThrow(() -> new EntityNotFoundException("Image not found."));
  }

  @Override
  public void deleteImageById(Long imageId) {
    imageRepository
        .findById(imageId)
        .ifPresentOrElse(
            image -> {
              imageRepository.delete(image);

              // After deleting the image, delete embeddings with this image id.
              EmbeddingsDeleteRequest request =
                  EmbeddingsDeleteRequest.builder()
                      .collectionName(collectionName)
                      .imageId(imageId.toString())
                      .build();

              chromaService.deleteEmbeddingsByCollectionId(request);
              log.info("Deleted image {} and its embeddings", imageId);
            },
            () -> {
              ;
              throw new EntityNotFoundException("Image not found.");
            });
  }

  @Transactional
  @Override
  public void updateImage(MultipartFile file, Long imageId, Long productId) throws IOException {
    Image image = getImageById(imageId);
    try {
      image.setFileName(file.getOriginalFilename());
      image.setFileType(file.getContentType());
      image.setImage(new SerialBlob(file.getBytes()));
      var savedImage = imageRepository.save(image);

      // After updating the image, it may not be relevant to exisiting embeddings anymore.
      // Delete those embeddings.
      EmbeddingsDeleteRequest deleteEmbeddings =
          EmbeddingsDeleteRequest.builder()
              .collectionName(collectionName)
              .imageId(imageId.toString())
              .build();
      chromaService.deleteEmbeddingsByCollectionId(deleteEmbeddings);

      String embeddedImageId = getImageSummary(productId, file, savedImage);
      log.info("Updated image embedded id : {} ", embeddedImageId);
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  // This method saves multiple images for a product.
  // It takes a product ID and a list of MultipartFile objects representing the images to be saved
  // for that product.
  // It retrieves the product by its ID, creates Image objects for each file,
  // sets the file name, file type, and image data, and associates each image with
  // the product.
  // It then saves each image to the database and constructs an ImageDto object for each saved
  // image, which includes the image ID, file name, and download URL.
  @Override
  public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
    Product product = productService.getProductById(productId);

    // It contains a list of saved ImageDto returned after saving to imageRepository.
    List<ImageDto> savedImages = new ArrayList<>();

    for (MultipartFile file : files) {
      try {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));
        image.setProduct(product);

        // Construct the download URL for the image
        // Assuming the base URL is "/api/v1/images/image/download/"
        // and the image ID will be appended to it.
        String buildDownloadUrl = "/api/v1/images/image/download/";

        // Save the image to the repository and set the actual download URL
        // After we save an image to the database, it will have an ID assigned to it.
        // We can then build the download URL using that ID.
        // The download URL is constructed by appending the image ID to the base URL.
        // This download URL will be used to retrieve the image later.
        Image savedImage = imageRepository.save(image);
        savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
        // Previously, image could be saved without a downloadUrl before because Hibernate only
        // commits to the database when the method ends.
        savedImage = imageRepository.save(savedImage);

        String embeddedImageId = getImageSummary(productId, file, savedImage);
        log.info("Stored image embedded Id: {}", embeddedImageId);

        // Create an ImageDto object to return the saved image details
        // The ImageDto object contains the image ID, file name, and download URL.
        // This DTO is used to transfer image data to the client side.
        ImageDto imageDto = new ImageDto();
        imageDto.setId(savedImage.getId());
        imageDto.setFileName(savedImage.getFileName());
        imageDto.setDownloadUrl(savedImage.getDownloadUrl());
        savedImages.add(imageDto);

      } catch (IOException | SQLException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
    return savedImages;
  }

  // creates and saves vector embeddings for an image.
  private String getImageSummary(Long productId, MultipartFile file, Image savedImage)
      throws IOException {
    // takes the image file, converts the visual content into embedding then stores the embedding in
    // the ChromaDB.
    return String.valueOf(imageSearchService.saveEmbedding(file, productId, savedImage.getId()));
  }
}
