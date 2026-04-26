package dev.hgjtu.auth_client.dto.market;

public class CommentRequest {
    private Long itemId;
    private Long replyCommentId;
    private String content;

    public CommentRequest(Long itemId, Long replyCommentId, String content) {
        this.itemId = itemId;
        this.replyCommentId = replyCommentId;
        this.content = content;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long postId) {
        this.itemId = postId;
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
