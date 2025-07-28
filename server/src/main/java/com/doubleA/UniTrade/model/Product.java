package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

// use Lombok Getter and Setter to reduce the boilerplate of writing Getter and Setter repeatedly for each field.
@Getter
@Setter

// NoArgsConstructor is for Hibernate to generate empty fields first using reflection then map values
// from the database to fields of Product.
@NoArgsConstructor

// Tell Hibernate to create Product entity in the database.
@Entity
public class Product {

// Set Id as the primary key and use auto-increment on it.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private Float price;
    private Integer inventory;
    private String description;

// Many Products belong to one Category.
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

// One Product can have many Images.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}
