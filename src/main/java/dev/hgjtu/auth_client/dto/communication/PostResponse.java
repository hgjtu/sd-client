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
    private UserWithMediaForResources authorInfo;
    private String content;
    private List<MediaResponse> medias;
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


    public String getContent() {
        return content;
    }

    public List<MediaResponse> getMedias() {
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

    public String getIsReacted() {
        return isReacted;
    }

    public UserWithMediaForResources getAuthorInfo() {
        return authorInfo;
    }
}
