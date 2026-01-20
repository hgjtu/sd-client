package dev.hgjtu.auth_client.dto.jump_group;

public class CommentRequest {
    private Integer groupId;
    private Long replyCommentId;
    private String content;

    public CommentRequest(Integer groupId, Long replyCommentId, String content) {
        this.groupId = groupId;
        this.replyCommentId = replyCommentId;
        this.content = content;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public String getContent() {
        return content;
    }
}
