package dev.hgjtu.auth_client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class JumpGroupService {
    private final WebClient webClient;

    @Value("${GATEWAY_SERVICE_URL}")
    private String gatewayServiceURL;
    @Value("${JUMP_GROUP_RESOURCE_PREFIX}")
    private String jumpGroupResourcePrefix;


}
