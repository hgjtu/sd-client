package dev.hgjtu.auth_client.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserEditRequest {
    private final String username;
//    private String email;
    private String firstName;
    private String lastName;
//    private String photoUrl;
    private String homeDropzone;
    private List<String> licenses;
    private String bio;

    public UserEditRequest(String username) {
        this.username = username;
    }
}
