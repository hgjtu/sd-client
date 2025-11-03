package dev.hgjtu.auth_client.dto.communication;

import java.util.List;

public class PostRequest {
    private Short sectionId;
    private Short categoryId;
    private Long userId;
    private String content;
    private List<String> mediaUrls;

    public PostRequest(Short sectionId, Short categoryId, Long userId, String content, List<String> mediaUrls) {
        this.sectionId = sectionId;
        this.categoryId = categoryId;
        this.userId = userId;
        this.content = content;
        this.mediaUrls = mediaUrls;
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

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setSectionId(Short sectionId) {
        this.sectionId = sectionId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
}