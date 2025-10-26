package dev.hgjtu.auth_client.dto.communication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PostResponse {
    private Long id;
    private Short sectionId;
    private Short categoryId;
    private Long userId;
    private String content;
    private List<String> mediaUrls;
    private LocalDateTime publicationDateTime;
    private Map<Short, Integer> smileyReactions;
}
