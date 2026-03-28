package dev.hgjtu.auth_client.dto.jump_group;

import dev.hgjtu.auth_client.dto.communication.UserWithMediaForResources;

import java.time.LocalDateTime;
import java.util.List;

public class GroupResponse {
    private Integer id;
    private UserWithMediaForResources organizerInfo;
    private Short trainingLevel;
    private String groupName;
    private String description;
    private LocalDateTime jumpDateTime;
    private Short maxParticipants;
    private List<UserWithMediaForResources> participants;
    private String jumpPlace;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;
    private Boolean participantBool;

    public GroupResponse(Integer id, UserWithMediaForResources organizerInfo, Short trainingLevel, String groupName, String description, LocalDateTime jumpDateTime, Short maxParticipants, List<UserWithMediaForResources> participants, String jumpPlace, LocalDateTime createdAt, List<CommentResponse> comments, Boolean participantBool) {
        this.id = id;
        this.organizerInfo = organizerInfo;
        this.trainingLevel = trainingLevel;
        this.groupName = groupName;
        this.description = description;
        this.jumpDateTime = jumpDateTime;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
        this.jumpPlace = jumpPlace;
        this.createdAt = createdAt;
        this.comments = comments;
        this.participantBool = participantBool;
    }

    public Integer getId() {
        return id;
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

    public List<UserWithMediaForResources> getParticipants() {
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

    public Boolean getParticipantBool() {
        return participantBool;
    }

    public UserWithMediaForResources getOrganizerInfo() {
        return organizerInfo;
    }
}
