//package dev.hgjtu.auth_client.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
//import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//
//@Configuration
//public class OAuth2LoggingConfig {
//
////    @Bean
////    public OAuth2AuthorizedClientService authorizedClientService() {
////        return new InMemoryOAuth2AuthorizedClientService(registeredClientRepository());
////    }
//
//    @EventListener
//    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
//        if (event.getAuthentication() instanceof OAuth2AuthenticationToken) {
//            System.out.println("OAuth2 authentication successful: " + event.getAuthentication());
//        }
//    }
//}