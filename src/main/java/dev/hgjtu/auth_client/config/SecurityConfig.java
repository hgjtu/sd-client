package dev.hgjtu.auth_client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/user", true)
                        .failureUrl("/login?error=true")
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestResolver(pkceResolver(clientRegistrationRepository()))
                        )
                        .tokenEndpoint(token -> token
                                .accessTokenResponseClient(accessTokenResponseClient())
                        )
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                );

        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(webClientRegistration());
    }

    private ClientRegistration webClientRegistration() {
        return ClientRegistration.withRegistrationId("web-client")
                .clientId("web-client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("read", "openid", "profile")
                .userInfoUri("http://localhost:9090/userinfo")
                .userNameAttributeName("sub")
                .authorizationUri("http://localhost:9090/oauth2/authorize")
                .tokenUri("http://localhost:9090/oauth2/token")
                .jwkSetUri("http://localhost:9090/oauth2/jwks")
                .clientName("Web Client")
                .build();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver pkceResolver(ClientRegistrationRepository clientRegistrationRepository) {
        var resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(builder -> {
            try {
                Map<String, String> pkceData = generatePkceCodes();
                String codeVerifier = pkceData.get("code_verifier");
                String codeChallenge = pkceData.get("code_challenge");

                // Добавляем PKCE параметры в authorization request
                builder.additionalParameters(params -> {
                    params.put(PkceParameterNames.CODE_CHALLENGE, codeChallenge);
                    params.put(PkceParameterNames.CODE_CHALLENGE_METHOD, "S256");
                });

                // Сохраняем code_verifier в сессии для использования при token request
                var attributes = RequestContextHolder.currentRequestAttributes();
                attributes.setAttribute("code_verifier", codeVerifier,
                        org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION);

                logger.info("Generated PKCE - Challenge: {}, Verifier saved in session", codeChallenge);

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to generate PKCE codes", e);
            }
        });

        return resolver;
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();

        // Создаем кастомный конвертер для добавления code_verifier
        client.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter() {
            @Override
            public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
                RequestEntity<?> entity = super.convert(authorizationCodeGrantRequest);

                // Получаем code_verifier из сессии
                var attributes = RequestContextHolder.currentRequestAttributes();
                String codeVerifier = (String) attributes.getAttribute("code_verifier",
                        org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION);

                if (codeVerifier != null && entity.getBody() instanceof MultiValueMap) {
                    @SuppressWarnings("unchecked")
                    MultiValueMap<String, String> body = (MultiValueMap<String, String>) entity.getBody();
                    body.add(PkceParameterNames.CODE_VERIFIER, codeVerifier);
                    logger.info("Added code_verifier to token request");
                }

                return entity;
            }
        });

        return client;
    }

    private Map<String, String> generatePkceCodes() throws NoSuchAlgorithmException {
        // Генерация code_verifier
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);

        // Генерация code_challenge
        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

        Map<String, String> pkceData = new HashMap<>();
        pkceData.put("code_verifier", verifier);
        pkceData.put("code_challenge", challenge);
        pkceData.put("method", "S256");

        logger.info("Generated PKCE codes - Verifier: {}, Challenge: {}", verifier, challenge);

        return pkceData;
    }
}