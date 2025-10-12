package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.UserEditRequest;
import dev.hgjtu.auth_client.dto.UserResponse;
import dev.hgjtu.auth_client.models.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient webClient;

    @Value("${USER_RESOURCE_SERVER_URL}")
    private String resourceServerUrl;

    public Mono<UserResponse> getUserInfoById(Long id) {
        return webClient.get()
                .uri(resourceServerUrl + "/api/user/id/{id}", id)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> getUserInfoByUsername(String username) {
        return webClient.get()
                .uri(resourceServerUrl + "/api/user/username/{username}", username)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> editUser(UserEditRequest userEditRequest) {
        return webClient.patch()
                .uri(resourceServerUrl + "/api/user/username/{username}", userEditRequest.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userEditRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }


}
