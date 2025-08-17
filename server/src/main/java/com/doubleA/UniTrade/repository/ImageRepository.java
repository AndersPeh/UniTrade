package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // This repository allows ImageService to interact with the database for image-related operations.
    // It extends JpaRepository to provide basic CRUD operations for Image entities.
}
