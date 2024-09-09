package com.example.chess.model;


import jakarta.persistence.*;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaderboardId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private int matchesPlayed;

    @Column(nullable = false)
    private int matchesWon;

    @Column(nullable = false)
    private int matchesLost;

    @Column(nullable = false)
    private int matchesDrawn;

    @Column(nullable = false)
    private int currentRanking;

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

