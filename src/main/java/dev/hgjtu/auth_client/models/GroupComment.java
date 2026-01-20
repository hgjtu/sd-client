package dev.hgjtu.auth_client.models;

import java.time.LocalDateTime;

public class GroupComment {
    private Long id;
    private Integer groupId;
    private Long userId;
    private Long replyCommentId;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public String getContent() {
        return content;
    }

    public Integer getLikes() {
        return likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
