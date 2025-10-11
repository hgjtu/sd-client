package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{id}")
    public Mono<String> userPage(Model model,
                                 @PathVariable Long id) { // @AuthenticationPrincipal OAuth2User principal
        return userService.getUserInfoById(id)
                .map(userInfo -> {
                    model.addAttribute("user", userInfo);
                    return "user/user-page";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("user/user-page");
                });
    }

    @GetMapping("/profile")
    public Mono<String> userPage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if(principal == null) { return Mono.just("error"); }
        return userService.getUserInfoByUsername(principal.getAttribute("sub"))
                .map(userInfo -> {
                    model.addAttribute("user", userInfo);
                    return "user/user-page";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("user/user-page");
                });
    }

//    @GetMapping("/user/my-page")
//    public Mono<String> userPage(Model model,
//                       @RegisteredOAuth2AuthorizedClient("web-client") OAuth2AuthorizedClient authorizedClient) {
//        return userService.getUserInfoById()
//                .map(userInfo -> {
//                    model.addAttribute("user", userInfo);
//                    return "user/user-page";
//                })
////                .onErrorResume(ClientAuthorizationRequiredException.class, e -> {
////                    // Перенаправляем на авторизацию
////                    return Mono.just("redirect:/oauth2/authorization/web-client");
////                })
//                .onErrorResume(Exception.class, e -> {
//                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
//                    return Mono.just("user/user-page");
//                });
////        try {
////            model.addAttribute("userInfo", userService.getUserInfo(authorizedClient));
////        } catch (Exception e) {
////            model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
////        }
////
////        return "user/user-page";
////        return "home";
//    }
}
