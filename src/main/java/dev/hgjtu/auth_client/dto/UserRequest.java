package dev.hgjtu.auth_client.dto;

import dev.hgjtu.auth_client.models.UserRole;

import java.util.List;


public class UserRequest {
    private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String homeDropzone;
    private List<String> licenses;
    private String bio;
}
