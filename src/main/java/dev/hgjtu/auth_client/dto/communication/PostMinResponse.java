//package dev.hgjtu.auth_client.dto.communication;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//public class PostMinResponse {
//    private Long id;
//    private Short sectionId;
//    private Short categoryId;
//    private String mainImageUrl;
//    private Long userId;
//    private String authorUsername;
//    private String content;
//    private LocalDateTime publicationDateTime;
//    private Map<String, Integer> smileyReactions;
//    private String isReacted;
//
//    public PostMinResponse(Long id, Short sectionId, Short categoryId, String mainImageUrl, Long userId, String authorUsername, String content, LocalDateTime publicationDateTime, Map<String, Integer> smileyReactions, String isReacted) {
//        this.id = id;
//        this.sectionId = sectionId;
//        this.categoryId = categoryId;
//        this.mainImageUrl = mainImageUrl;
//        this.userId = userId;
//        this.authorUsername = authorUsername;
//        this.content = content;
//        this.publicationDateTime = publicationDateTime;
//        this.smileyReactions = smileyReactions;
//        this.isReacted = isReacted;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public Short getSectionId() {
//        return sectionId;
//    }
//
//    public Short getCategoryId() {
//        return categoryId;
//    }
//
//    public String getMainImageUrl() {
//        return mainImageUrl;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public String getAuthorUsername() {
//        return authorUsername;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public LocalDateTime getPublicationDateTime() {
//        return publicationDateTime;
//    }
//
//    public Map<String, Integer> getSmileyReactions() {
//        return smileyReactions;
//    }
//
//    public String getIsReacted() {
//        return isReacted;
//    }
//}
