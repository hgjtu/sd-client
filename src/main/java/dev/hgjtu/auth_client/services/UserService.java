package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.models.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient webClient;

    @Value("${RESOURCE_SERVER_URL}")
    private String resourceServerUrl;

    public Mono<User> getUserInfo() {
        return webClient.get()
                .uri(resourceServerUrl + "/api/user")
                .retrieve()
                .bodyToMono(User.class);
    }

    public Mono<User> getUserInfoById(Long id) {
        return webClient.get()
                .uri(resourceServerUrl + "/api/user/{id}", id)
                .retrieve()
                .bodyToMono(User.class);
    }
}
