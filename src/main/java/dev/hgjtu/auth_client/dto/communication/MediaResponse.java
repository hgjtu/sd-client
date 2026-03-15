package dev.hgjtu.auth_client.dto.communication;

import dev.hgjtu.auth_client.models.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

public class MediaResponse {
    private UUID id;
    private String uploadUrl;
    private MediaType type;

    public MediaResponse(UUID id, String uploadUrl, MediaType type) {
        this.id = id;
        this.uploadUrl = uploadUrl;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public MediaType getType() {
        return type;
    }

    public String getTypeAsString() {
        return type.name();
    }
}
