package dev.hgjtu.auth_client.dto.jump_group;

import java.time.LocalDateTime;

public class GroupRequest {
    private Short trainingLevel;
    private String groupName;
    private String description;
    private LocalDateTime jumpDateTime;
    private Short maxParticipants;
    private String jumpPlace;

    public GroupRequest(Short trainingLevel, String groupName, String description, LocalDateTime jumpDateTime, Short maxParticipants, String jumpPlace) {
        this.trainingLevel = trainingLevel;
        this.groupName = groupName;
        this.description = description;
        this.jumpDateTime = jumpDateTime;
        this.maxParticipants = maxParticipants;
        this.jumpPlace = jumpPlace;
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

    public String getJumpPlace() {
        return jumpPlace;
    }
}
