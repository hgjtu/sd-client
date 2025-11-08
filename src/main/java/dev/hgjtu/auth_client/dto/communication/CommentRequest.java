package dev.hgjtu.auth_client.dto.communication;

public class CommentRequest {
    private Long postId;
    private Long replyCommentId;
    private String content;

    public CommentRequest(Long postId, Long replyCommentId, String content) {
        this.postId = postId;
        this.replyCommentId = replyCommentId;
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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
}
