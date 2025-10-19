package dev.hgjtu.auth_client.dto.user;

import dev.hgjtu.auth_client.models.Jump;
import dev.hgjtu.auth_client.models.UserRole;
import lombok.Getter;

import java.util.List;

@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String homeDropzone;
    private List<String> licenses;
    private String bio;
    private List<Jump> jumps;
}
