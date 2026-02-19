package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.MediaUploadResponse;
import dev.hgjtu.auth_client.dto.UploadUrlRequest;
import dev.hgjtu.auth_client.models.AvailableResources;
import dev.hgjtu.auth_client.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping("/media/{resource-name}/upload-url")
    public Mono<MediaUploadResponse> uploadUrl(@RequestBody UploadUrlRequest uploadUrlRequest,
                                               @PathVariable("resource-name") AvailableResources resourceName) {
        return mediaService.getUploadUrl(resourceName, uploadUrlRequest);
    }

    @PostMapping("/media/{resource-name}/complete/{mediaId}")
    public Mono<MediaUploadResponse> completeMedia(@PathVariable("resource-name") AvailableResources resourceName,
                                                   @PathVariable UUID mediaId) {
        return mediaService.completeMedia(resourceName, mediaId);
    }
}
