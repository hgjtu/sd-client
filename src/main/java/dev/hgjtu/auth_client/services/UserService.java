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

    @Value("${GATEWAY_SERVICE_URL}")
    private String gatewayServiceURL;
    @Value("${USER_RESOURCE_PREFIX}")
    private String userResourcePrefix;

    public Mono<UserResponse> getUserInfoById(Long id) {
        return webClient.get()
                .uri(gatewayServiceURL + userResourcePrefix + "/users/id/{id}", id)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> getUserInfoByUsername(String username) {
        return webClient.get()
                .uri(gatewayServiceURL + userResourcePrefix + "/users/username/{username}", username)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> editUser(UserEditRequest userEditRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + userResourcePrefix + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userEditRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> addJump(JumpRequest jumpRequest){
        return webClient.post()
                .uri(gatewayServiceURL + userResourcePrefix + "/logbook/addJump")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jumpRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> editJump(Long id, JumpRequest jumpRequest){
        return webClient.patch()
                .uri(gatewayServiceURL + userResourcePrefix + "/logbook/editJump/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jumpRequest)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Mono<UserResponse> deleteJump(Long id){
        return webClient.delete()
                .uri(gatewayServiceURL + userResourcePrefix + "/logbook/deleteJump/{id}", id)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }
}
