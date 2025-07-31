package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// use Lombok Getter and Setter to reduce the boilerplate of writing Getter and Setter repeatedly for each field.
@Getter
@Setter

// NoArgsConstructor is for Hibernate to generate empty fields first using reflection then map values
// from the database to fields of Product.
@NoArgsConstructor

// Tell JPA to create Product entity in the database.
@Entity
public class Product {

// Tell JPA to set Id as the primary key.
    @Id
// GenerationType.IDENTITY tells JPA to automatically generate a unique value for the primary key
// using auto-increment. Hibernate implements this by generating SQL queries based on Java code.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// These fields are mapped to columns in the database by JPA automatically because of @Entity.
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer inventory;
    private String description;

// Tell JPA to define Many-to-One relationship between Product and Category.
// Many Products belong to one Category.
// Hibernate implements this relationship mapping in the database.
// CascadeType.ALL means actions on Product entity will be done automatically on associated entities.
// For example, when a Product is deleted, its Category object (if not referenced by other product)
// and Image objects will also be deleted (because of orphanRemoval=true).
    @ManyToOne(cascade = CascadeType.ALL)
// Because Product is the owning side, it has a column named category_id as foreign key.
// @JoinColumn tells JPA to specify category_id as foreign key for Product entity.
// Hibernate executes this by creating the foreign key
// in the database referencing the primary key of the Category entity.
    @JoinColumn(name = "category_id")
    private Category category;

// Tell JPA to define One to Many relationship between Product and Image.
// One Product can have many Images.
// As Image is the owning side, need to write mappedBy = "product" in Product class
// to tell JPA that the relationship (foreign key) is managed in the Image entity.
// so product field in Image entity is the foreign key.
// orphanRemoval = true means if an image is deleted in the Product entity,
// Hibernate will delete that image from the database.
// Without orphanRemoval = true, when an image is deleted from Product's images list,
// the image will not be associated with Product but still exist in the Image entity.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

// Product is the inverse side, when it is deleted, associated CartItem should also be deleted.
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<CartItem> cartItems=new HashSet<>();

// Product is the inverse side, when it is deleted, associated OrderItem should also be deleted.
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<OrderItem> orderItems= new HashSet<>();

// Constructor excluding Id and images. Because Id is automatically generated and images is optional.
// Everything in the constructor must be provided when creating Product.
    public Product(String name, String brand, BigDecimal price, Integer inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }
}
