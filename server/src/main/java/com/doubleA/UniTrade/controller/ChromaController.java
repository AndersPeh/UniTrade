package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.request.EmbeddingsDeleteRequest;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.chroma.ChromaService;
import com.doubleA.UniTrade.service.chroma.IChromaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/chroma")
public class ChromaController {
  private final IChromaService chromaService;

  @GetMapping("/collections")
  public ResponseEntity<ApiResponse> listCollections() {
    List<Collection> collections = chromaService.listCollections();
    return ResponseEntity.ok(new ApiResponse("collections", collections));
  }

  @GetMapping("/collection/{collectionName}")
  public ResponseEntity<ApiResponse> getCollectionByName(@PathVariable String collectionName) {
    Collection collection = chromaService.getCollectionByName(collectionName);
    return ResponseEntity.ok(new ApiResponse("collections", collection));
  }

  @DeleteMapping("/collections/{collectionName}/delete")
  public ResponseEntity<ApiResponse> deleteCollection(@PathVariable String collectionName) {
    chromaService.deleteCollection(collectionName);
    return ResponseEntity.ok(
        new ApiResponse("Collection ", collectionName + " was successfully deleted"));
  }

  @DeleteMapping("/collection/embeddings/delete")
  public ResponseEntity<ApiResponse> deleteEmbeddings(
      @RequestBody EmbeddingsDeleteRequest request) {
    chromaService.deleteEmbeddingsByCollectionId(request);
    return ResponseEntity.ok(new ApiResponse("Embeddings deleted successfully.", null));
  }

  @GetMapping("/collection/{collectionId}/embeddings")
  public ResponseEntity<ApiResponse> getEmbeddings(@PathVariable String collectionId) {
    ChromaApi.GetEmbeddingResponse embeddings = chromaService.getEmbeddings(collectionId);
    log.info("embeddings: {}", embeddings);
    return ResponseEntity.ok(new ApiResponse("Success", embeddings));
  }
}
