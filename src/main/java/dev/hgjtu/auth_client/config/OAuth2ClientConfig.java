//package dev.hgjtu.auth_client.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.*;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class OAuth2ClientConfig {
//
//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientService authorizedClientService) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider =
//                OAuth2AuthorizedClientProviderBuilder.builder()
//                        .authorizationCode()
//                        .refreshToken()
//                        .build();
//
//        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository, authorizedClientService);
//
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        // Добавляем поддержку PKCE
//        authorizedClientManager.setContextAttributesMapper(authorizeRequest -> {
//            Map<String, Object> contextAttributes = new HashMap<>();
//            HttpServletRequest request = ((OAuth2AuthorizationContext) authorizeRequest).getAttribute(HttpServletRequest.class.getName());
//
//            if (request != null) {
//                String codeVerifier = (String) request.getSession().getAttribute("code_verifier");
//                if (codeVerifier != null) {
//                    contextAttributes.put(PkceParameterNames.CODE_VERIFIER, codeVerifier);
//                }
//            }
//            return contextAttributes;
//        });
//
//        return authorizedClientManager;
//    }
//
//    @Bean
//    public OAuth2AuthorizedClientService authorizedClientService(
//            ClientRegistrationRepository clientRegistrationRepository) {
//        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
//    }
//}
