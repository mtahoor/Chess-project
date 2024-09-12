package com.example.chess.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;


    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;


    @Getter
    @Setter
    @Column(nullable = false)
    private String name;




    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    private String profilePicture; 

    @Getter
    @Setter
    @Column(nullable = false)
    private Integer rating;


    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Getter
    @Setter
    @OneToMany(mappedBy = "player1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> player1Matches;


    @Getter
    @Setter
    @OneToMany(mappedBy = "player2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> player2Matches;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new java.util.Date();
    }
}

