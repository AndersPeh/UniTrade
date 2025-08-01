package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@Entity

// AllArgsConstructor because product is compulsory to have when creating Image entity.
@AllArgsConstructor

public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;

// Tells JPA to store the image property as a Large Object (LOB),
// because images can be large and cant be stored as regular columns.
    @Lob
    private Blob image;
    private String downloadUrl;

// Add a column named product_id to Image entity as foreign key to establish Many Image to One Product relationship.
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
