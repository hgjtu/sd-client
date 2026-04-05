package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.ChangePasswordRequest;
import dev.hgjtu.auth_client.dto.user.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final WebClient webClient;
    private final WebClient serverWebClient;

    @Value("${AUTH_SERVER_URL}")
    private String authServerUrl;

    @Value("${GATEWAY_SERVICE_URL}")
    private String gatewayServiceURL;
    @Value("${USER_RESOURCE_PREFIX}")
    private String userResourcePrefix;

    public Mono<Void> registerUser(RegistrationRequest registerRequest) {
        return serverWebClient.post()
                .uri(authServerUrl + "/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .then(serverWebClient.post()
                        .uri(gatewayServiceURL + userResourcePrefix + "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(registerRequest)
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    public Mono<ResponseEntity<Void>> changePassword(ChangePasswordRequest changePasswordRequest) {
        return webClient.post()
                .uri(authServerUrl + "/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(changePasswordRequest)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<ResponseEntity<Void>> changeEmail(String newEmail) {
        return webClient.post()
                .uri(authServerUrl + "/auth/change-email")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newEmail)
                .retrieve()
                .toBodilessEntity()
                .then(serverWebClient.post()
                        .uri(gatewayServiceURL + userResourcePrefix + "/auth/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(newEmail)
                        .retrieve()
                        .toBodilessEntity());
    }
}
