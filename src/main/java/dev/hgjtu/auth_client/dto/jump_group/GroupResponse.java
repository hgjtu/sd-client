package dev.hgjtu.auth_client.dto.jump_group;

import java.time.LocalDateTime;
import java.util.List;

public class GroupResponse {
    private Integer id;
    private Long organizerId;
    private String organizerUsername;
    private Short trainingLevel;
    private String groupName;
    private String description;
    private LocalDateTime jumpDateTime;
    private Short maxParticipants;
    private List<Long> participants;
    private String jumpPlace;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;
    private Boolean isParticipant;

    public GroupResponse(Integer id, Long organizerId, String organizerUsername, Short trainingLevel, String groupName, String description, LocalDateTime jumpDateTime, Short maxParticipants, List<Long> participants, String jumpPlace, LocalDateTime createdAt, List<CommentResponse> comments, Boolean isParticipant) {
        this.id = id;
        this.organizerId = organizerId;
        this.organizerUsername = organizerUsername;
        this.trainingLevel = trainingLevel;
        this.groupName = groupName;
        this.description = description;
        this.jumpDateTime = jumpDateTime;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
        this.jumpPlace = jumpPlace;
        this.createdAt = createdAt;
        this.comments = comments;
        this.isParticipant = isParticipant;
    }

    public Integer getId() {
        return id;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public String getOrganizerUsername() {
        return organizerUsername;
    }

    public Short getTrainingLevel() {
        return trainingLevel;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getJumpDateTime() {
        return jumpDateTime;
    }

    public Short getMaxParticipants() {
        return maxParticipants;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public String getJumpPlace() {
        return jumpPlace;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public Boolean getIsParticipant() {
        return isParticipant;
    }
}
