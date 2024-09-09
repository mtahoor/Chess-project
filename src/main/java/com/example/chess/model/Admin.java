package com.example.chess.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Link to User for authentication

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    // Getters and setters

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new java.util.Date();
    }
}
