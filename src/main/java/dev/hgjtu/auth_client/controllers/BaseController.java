package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.ChangePasswordRequest;
import dev.hgjtu.auth_client.dto.user.RegistrationRequest;
import dev.hgjtu.auth_client.services.BaseService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BaseController {

    private final BaseService baseService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            model.addAttribute("status", status.toString());
        }
        return "error";
    }

    @GetMapping("/register")
    public Mono<String> showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return Mono.just("register");
    }

    @PostMapping("/register")
    public Mono<String> register(@ModelAttribute RegistrationRequest registrationRequest) {
        return baseService.registerUser(registrationRequest)
                .then(Mono.just("redirect:/login?registered"));
    }

    @GetMapping("/change-password")
    public Mono<String> changePasswordForm(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return Mono.just("change-password");
    }

    @PostMapping("/change-password")
    public Mono<ResponseEntity<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return baseService.changePassword(changePasswordRequest)
                .flatMap(responseEntity -> Mono.just(ResponseEntity
                        .status(responseEntity.getStatusCode())
                        .build()));
    }

    @PostMapping("/profile/change-email")
    public Mono<ResponseEntity<Void>> changeEmail(@RequestBody String newEmail) {
        return baseService.changeEmail(newEmail)
                .flatMap(responseEntity -> Mono.just(ResponseEntity
                        .status(responseEntity.getStatusCode())
                        .build()));
    }
}