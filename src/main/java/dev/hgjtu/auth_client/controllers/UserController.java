package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String home(Model model,
                       @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        try {
            model.addAttribute("userInfo", userService.getUserInfo());
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
        }

        return "user/user-page";
    }

}
