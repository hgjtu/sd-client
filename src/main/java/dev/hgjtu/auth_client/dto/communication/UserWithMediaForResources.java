package dev.hgjtu.auth_client.dto.communication;

public class UserWithMediaForResources {
    private Long id;
    private String username;
    private String profileMediaUrl;

    public UserWithMediaForResources() {
    }

    public UserWithMediaForResources(Long id, String username, String profileMediaUrl) {
        this.id = id;
        this.username = username;
        this.profileMediaUrl = profileMediaUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileMediaUrl() {
        return profileMediaUrl;
    }

    public void setProfileMediaUrl(String profileMediaUrl) {
        this.profileMediaUrl = profileMediaUrl;
    }
}
