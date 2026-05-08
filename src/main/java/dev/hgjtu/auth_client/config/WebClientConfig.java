package dev.hgjtu.auth_client.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${spring.security.oauth2.client.registration.web-client.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.web-client-cc.client-id}")
    private String ccClientId;

//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientService clientService) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider =
//                OAuth2AuthorizedClientProviderBuilder.builder()
//                        .refreshToken()
//                        .clientCredentials()
//                        .build();
//
//        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
//                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
//
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken(configurer -> configurer.clockSkew(Duration.ofSeconds(60)))
                        .clientCredentials()
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientRepository
                );

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId(clientId);

        return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    @Bean
    public WebClient serverWebClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        return WebClient.builder().build();
    }

//    @Bean
//    public WebClient publicWebClient(OAuth2AuthorizedClientManager authorizedClientManager) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//
//        oauth2.setDefaultClientRegistrationId(ccClientId);
//
//        return WebClient.builder()
//                .apply(oauth2.oauth2Configuration())
//                .build();
//    }
}
