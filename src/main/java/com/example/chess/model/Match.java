package com.example.chess.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @ManyToOne
    @JoinColumn(name = "player_1_id", nullable = false)
    private Student player1;

    @ManyToOne
    @JoinColumn(name = "player_2_id", nullable = false)
    private Student player2;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date matchDate;

    @Column(nullable = false)
    private String result;  // WIN, LOSS, DRAW

    @Column(nullable = true)
    private String chessComUrl;

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
