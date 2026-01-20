package dev.hgjtu.auth_client.dto.jump_group;



import dev.hgjtu.auth_client.models.GroupComment;

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
}
