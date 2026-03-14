package dev.hgjtu.auth_client.dto.communication;

import dev.hgjtu.auth_client.dto.MediaUploadResponse;
import dev.hgjtu.auth_client.models.PostComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PostResponse {
    private Long id;
    private Short sectionId;
    private Short categoryId;
    private Long userId;
    private String authorUsername;
    private String content;
    private List<MediaUploadResponse> medias;
    private LocalDateTime publicationDateTime;
    private Map<String, Integer> smileyReactions;
    private List<PostComment> comments;
    private String isReacted;

    public Long getId() {
        return id;
    }

    public Short getSectionId() {
        return sectionId;
    }

    public Short getCategoryId() {
        return categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public List<MediaUploadResponse> getMedias() {
        return medias;
    }

    public LocalDateTime getPublicationDateTime() {
        return publicationDateTime;
    }

    public Map<String, Integer> getSmileyReactions() {
        return smileyReactions;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getIsReacted() {
        return isReacted;
    }
}
