package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.user.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final WebClient serverWebClient;

    @Value("${AUTH_SERVER_URL}")
    private String authServerUrl;

    @Value("${USER_RESOURCE_SERVER_URL}")
    private String resourceServerUrl;

    public Mono<Void> registerUser(RegistrationRequest registerRequest) {
        return serverWebClient.post()
                .uri(authServerUrl + "/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .then(serverWebClient.post()
                        .uri(resourceServerUrl + "/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(registerRequest)
                        .retrieve()
                        .bodyToMono(Void.class));
    }
}
