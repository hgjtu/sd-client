package dev.hgjtu.auth_client.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient webClient;

    @Value("${RESOURCE_SERVER_URL}")
    private String resourceServerUrl;

    public String getUserInfo() {
        return webClient.get()
                .uri(resourceServerUrl + "/api/user")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
