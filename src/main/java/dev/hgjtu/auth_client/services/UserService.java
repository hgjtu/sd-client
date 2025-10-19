package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.user.JumpRequest;
import dev.hgjtu.auth_client.dto.user.UserEditRequest;
import dev.hgjtu.auth_client.dto.user.UserResponse;
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
                .uri(resourceServerUrl + "/api/users/id/{id}", id)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> getUserInfoByUsername(String username) {
        return webClient.get()
                .uri(resourceServerUrl + "/api/users/username/{username}", username)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> editUser(UserEditRequest userEditRequest) {
        return webClient.patch()
                .uri(resourceServerUrl + "/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userEditRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> addJump(JumpRequest jumpRequest){
        return webClient.post()
                .uri(resourceServerUrl + "/api/logbook/addJump")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jumpRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> editJump(Long id, JumpRequest jumpRequest){
        return webClient.patch()
                .uri(resourceServerUrl + "/api/logbook/editJump/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jumpRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> deleteJump(Long id){
        return webClient.delete()
                .uri(resourceServerUrl + "/api/logbook/deleteJump/{id}", id)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }
}
