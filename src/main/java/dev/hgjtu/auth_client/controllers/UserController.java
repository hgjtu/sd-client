package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.JumpRequest;
import dev.hgjtu.auth_client.dto.RegistrationRequest;
import dev.hgjtu.auth_client.dto.UserEditRequest;
import dev.hgjtu.auth_client.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/profile/edit")
    public Mono<String> showEditUserPage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        return userService.getUserInfoByUsername(principal.getAttribute("sub"))
                .map(userInfo -> {
                    model.addAttribute("userEditRequest", new UserEditRequest(
                            principal.getAttribute("sub"), userInfo.getFirstName(), userInfo.getLastName(),
                            userInfo.getHomeDropzone(), userInfo.getLicenses(), userInfo.getBio()));
                    return "user/user-edit-page";
                });
    }

    @PostMapping("/profile/edit")
    public Mono<String> editUser(@ModelAttribute UserEditRequest userEditRequest) {
        return userService.editUser(userEditRequest)
                .then(Mono.just("redirect:/profile"));
    }

    @PostMapping("/logbook/add")
    public Mono<String> addJump (@ModelAttribute JumpRequest jumpRequest) {
        return userService.addJump(jumpRequest)
                .then(Mono.just("redirect:/profile"));
    }

    @PostMapping("/logbook/edit/{id}")
    public Mono<String> editJump (@PathVariable Long id, @ModelAttribute JumpRequest jumpRequest) {
        return userService.editJump(id, jumpRequest)
                .then(Mono.just("redirect:/profile"));
    }

    @GetMapping("/logbook/delete/{id}")
    public Mono<String> deleteJump (@PathVariable Long id) {
        return userService.deleteJump(id)
                .then(Mono.just("redirect:/profile"));
    }
}
