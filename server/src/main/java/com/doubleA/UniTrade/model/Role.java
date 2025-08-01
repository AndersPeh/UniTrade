package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

// Because Role entity is selected to be the inverse side, just need to use mappedBy to
// refers roles field in User entity is establishing the Many-to-Many relationship.
    @ManyToMany(mappedBy = "roles")
// When using Collection, we can change the datatype anytime. Currently, intialised as HashSet which can be changed.
    private Collection<User> users = new HashSet<>();

// Id is auto-generated and User is optional.
    public Role(String name) {
        this.name = name;
    }
}
