package com.example.chess.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Getter
    private String email;

    @Getter
    @Column(unique = true)
    private String username;

    @Getter
    private String password;

    @Getter
    @Setter
    private boolean enabled;

    // Instead of @ManyToMany, use @ManyToOne to store role_id directly in the users table
    @Getter
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Constructors, Getters, and Setters

    public User() {}

    public User(String email, String username, String password, boolean enabled, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
    }
}
