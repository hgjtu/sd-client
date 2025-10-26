package dev.hgjtu.auth_client.dto.communication;

import java.util.List;

public class PostRequest {
    private Short sectionId;
    private Short categoryId;
    private Long userId;
    private String content;
    private List<String> mediaUrls;
}