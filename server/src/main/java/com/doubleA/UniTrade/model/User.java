package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Remove;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

// Tells JPA that this must be unique so Hibernate can use it to identify a User in queries and caching.
    @NaturalId
    private String email;
    private String password;

// If a user is deleted, associated cart and orders of the user should also be deleted.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

// When user is deleted, the role should remain so dont put cascade = CascadeType.ALL, orphanRemoval = true.
// fetch = FetchType.EAGER means when a User entity is loaded, its associated Role entities are loaded immediately,
// so roles of user is always available when a user is retrieved.
    @ManyToMany(fetch = FetchType.EAGER,
// Cascade.ALL includes Remove but dont want roles to be deleted automatically,
// so explicitly state CascadeType except ALL and REMOVE.
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})

// As Role is selected to be the inverse side using mappedBy, User is the owning side.
// The owning side has to configure the join table (user_roles), define 2 primary/ foreign keys (user_id, role_id),
// joinColumns refers to the id in owning side (User), inverseJoinColumns refers to the id in inverse side (Role).
    @JoinTable(name = "user_roles",
// user_id of user_roles JoinTable refers to id in User entity.
            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
// role_id of user_roles JoinTable refers to id in Role entity.
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
// This Many to Many with Join table method can't store additional information,
// it can only store user_id and role_id. Need to use Many to Many via explicit entity like CartItem to store more infomation.
    private Collection<Role> roles = new HashSet<>();
}
























