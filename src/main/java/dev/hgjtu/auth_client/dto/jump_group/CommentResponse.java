package dev.hgjtu.auth_client.dto.jump_group;



import dev.hgjtu.auth_client.models.GroupComment;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentResponse {
    private GroupComment comment;
    private Boolean isParticipant;

    public CommentResponse() {
    }

    public CommentResponse(GroupComment comment, Boolean isParticipant) {
        this.comment = comment;
        this.isParticipant = isParticipant;
    }

    public GroupComment getComment() {
        return comment;
    }

    public void setComment(GroupComment comment) {
        this.comment = comment;
    }

    public Boolean getParticipant() {
        return isParticipant;
    }

    public void setParticipant(Boolean participant) {
        isParticipant = participant;
    }
}
