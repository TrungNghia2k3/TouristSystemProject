package com.ntn.tourism.dto;

import lombok.Data;

@Data
public class UserRegisteredDTO {
    private Integer id;
    private String fullName;
    private String email;
    private String password;
    private String role;
}