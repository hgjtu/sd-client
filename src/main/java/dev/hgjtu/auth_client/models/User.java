package dev.hgjtu.auth_client.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class User {
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
}
