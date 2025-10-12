package dev.hgjtu.auth_client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserEditRequest {
    private final String username;
//    private String email;
    private String firstName;
    private String lastName;
//    private String photoUrl;
    private String homeDropzone;
    private List<String> licenses;
    private String bio;
}
