package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/my-page")
    public Mono<String> userPage(Model model,
                       @RegisteredOAuth2AuthorizedClient("web-client") OAuth2AuthorizedClient authorizedClient) {
        return userService.getUserInfo()
                .map(userInfo -> {
                    model.addAttribute("user", userInfo);
                    return "user/user-page";
                })
                .onErrorResume(ClientAuthorizationRequiredException.class, e -> {
                    // Перенаправляем на авторизацию
                    return Mono.just("redirect:/oauth2/authorization/web-client");
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("user/user-page");
                });

//        try {
//            model.addAttribute("userInfo", userService.getUserInfo(authorizedClient));
//        } catch (Exception e) {
//            model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
//        }
//
//        return "user/user-page";
//        return "home";
    }

    @GetMapping("/user/{id}")
    public String userPage(Model model,
                           @PathVariable Long id) {
        try {
            model.addAttribute("userInfo", userService.getUserInfoById(id));
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
        }

        return "user/user-page";
    }

}
