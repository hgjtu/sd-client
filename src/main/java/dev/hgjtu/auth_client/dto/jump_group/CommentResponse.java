package dev.hgjtu.auth_client.dto.jump_group;



import dev.hgjtu.auth_client.models.GroupComment;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private Integer groupId;
    private Long userId;
    private Long replyCommentId;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt;
    private Boolean isParticipant;

    public CommentResponse() {
    }

    public CommentResponse(GroupComment comment, Boolean isParticipant) {
        this.id = comment.getId();
        this.groupId = comment.getGroupId();
        this.userId = comment.getUserId();
        this.replyCommentId = comment.getReplyCommentId();
        this.content = comment.getContent();
        this.likes = comment.getLikes();
        this.createdAt = comment.getCreatedAt();
        this.isParticipant = isParticipant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getParticipant() {
        return isParticipant;
    }

    public void setParticipant(Boolean participant) {
        isParticipant = participant;
    }
}
