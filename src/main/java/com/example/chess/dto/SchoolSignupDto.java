package com.example.chess.dto;

import lombok.Getter;

public class SchoolSignupDto {
    @Getter
    private String email;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String name;    // School name
    @Getter
    private String address; // School address
}
