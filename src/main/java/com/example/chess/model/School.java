package com.example.chess.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "schools")
public class School {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;


    @Getter
    @Setter
    @Column(nullable = false)
    private String address;



    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Link to User for authentication


    @Getter
    @Setter
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

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
