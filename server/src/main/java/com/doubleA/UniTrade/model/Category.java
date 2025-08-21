package com.doubleA.UniTrade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

// No CascadeType.ALL because it doesnt make sense to delete a category,
// then all associated products are deleted.
// As long as a product is still referring to a category, the category can't be deleted due to referential integrity.
// category is placed in Product (owning) side as foreign key to establish the One Category to Many Product relationship.

    @JsonIgnore // Prevents getter method or field from infinite recursion when serializing to JSON.
    @OneToMany(mappedBy = "category")
    private List<Product> products;

// Exclude id and products because id is auto-generated and products is optional.
    public Category(String name) {
        this.name = name;
    }
}
