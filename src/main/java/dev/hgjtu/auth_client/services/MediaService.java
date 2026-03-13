package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.MediaUploadResponse;
import dev.hgjtu.auth_client.dto.UploadUrlRequest;
import dev.hgjtu.auth_client.dto.user.JumpRequest;
import dev.hgjtu.auth_client.dto.user.UserResponse;
import dev.hgjtu.auth_client.models.AvailableResources;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final WebClient webClient;

    @Value("${GATEWAY_SERVICE_URL}")
    private String gatewayServiceURL;
    @Value("${USER_RESOURCE_PREFIX}")
    private String userResourcePrefix;
    @Value("${JUMP_GROUP_RESOURCE_PREFIX}")
    private String jumpGroupResourcePrefix;
    @Value("${COMMUNICATION_RESOURCE_PREFIX}")
    private String communicationResourcePrefix;
    @Value("${MARKET_RESOURCE_PREFIX}")
    private String marketResourcePrefix;

    public String selectResource(AvailableResources resourceName){
        String resourcePrefix = "";

        switch (resourceName){
            case USERS -> resourcePrefix += userResourcePrefix;
            case MARKET -> resourcePrefix += marketResourcePrefix;
            case COMMUNICATION -> resourcePrefix += communicationResourcePrefix;
            case JUMPGROUPS -> resourcePrefix += jumpGroupResourcePrefix;
        }

        return resourcePrefix;
    }

    public Mono<MediaUploadResponse> getUploadUrl(AvailableResources resourceName, UploadUrlRequest uploadUrlRequest){
        return webClient.post()
                .uri(gatewayServiceURL + selectResource(resourceName) + "/media/upload-url")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(uploadUrlRequest)
                .retrieve()
                .bodyToMono(MediaUploadResponse.class);
    }

    public Mono<MediaUploadResponse> getUploadUrlForItem(Long itemId, AvailableResources resourceName, UploadUrlRequest uploadUrlRequest){
        return webClient.post()
                .uri(gatewayServiceURL + selectResource(resourceName) + "/media/upload-url/item/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(uploadUrlRequest)
                .retrieve()
                .bodyToMono(MediaUploadResponse.class);
    }

    public Mono<MediaUploadResponse> completeMedia(AvailableResources resourceName, UUID mediaId){
        return webClient.post()
                .uri(gatewayServiceURL + selectResource(resourceName) + "/media/complete/{id}", mediaId)
                .retrieve()
                .bodyToMono(MediaUploadResponse.class);
    }

}
