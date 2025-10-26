package dev.hgjtu.auth_client.models;

import lombok.Getter;

@Getter
public class ItemComment {
    private Long id;
    private Long itemId;
    private Long userId;
    private Long replyCommentId;
    private String content;
}
