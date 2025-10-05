package com.doubleA.UniTrade.service.embeddings;

import com.doubleA.UniTrade.utils.LLMServiceUtil;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSearchService {
  private final ChromaVectorStore vectorStore;
  private final LLMServiceUtil llmServiceUtil;

  // Whenever an image is upploaded or updated, create an embedding for it and save it to the
  // ChromaDB. Although productId is not needed to look for the embedding (use image id),
  // it is needed when performing image search. After performing image search,
  // product id is needed to know which product the embedding is associated with
  // to display products similar to the image provided for searching.
  public List<String> saveEmbeddings(MultipartFile image, Long productId, Long imageId)
      throws IOException, java.io.IOException {
    String imageDescription = llmServiceUtil.descriptionImage(image);
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("productId", productId);
    metadata.put("imageId", imageId.toString());
    metadata.put("documentId", UUID.randomUUID().toString());
    var doc =
        Document.builder().id(imageId.toString()).text(imageDescription).metadata(metadata).build();
    try {
      vectorStore.doAdd(List.of(doc));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return List.of("Successfully added to vector store");
  }

  public List<Long> searchImageSimilarity(MultipartFile queryImage)
      throws IOException, java.io.IOException {
    String imageDescription = llmServiceUtil.descriptionImage(queryImage);
    SearchRequest searchRequest =
        SearchRequest.builder().query(imageDescription).topK(10).similarityThreshold(0.85f).build();
    List<Document> searchResult = vectorStore.doSimilaritySearch(searchRequest);
    log.info("Search result: {}", searchResult);
    searchResult.forEach(
        doc -> {
          Double score = doc.getScore();
          Object productId = doc.getMetadata().get("productId");
          log.info("Found doc with productId: {}, similarity score: {}", productId, score);
        });
    return searchResult.stream()
        .map(doc -> doc.getMetadata().get("productId"))
        .filter(Objects::nonNull)
        .map(Object::toString)
        .map(Long::valueOf)
        .collect(Collectors.toList());
  }
}
