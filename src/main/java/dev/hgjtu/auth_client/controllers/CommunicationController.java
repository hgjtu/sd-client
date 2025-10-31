package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.communication.CategoryResponse;
import dev.hgjtu.auth_client.dto.communication.PostResponse;
import dev.hgjtu.auth_client.dto.communication.SectionResponse;
import dev.hgjtu.auth_client.dto.communication.SmileyReactionResponse;
import dev.hgjtu.auth_client.dto.user.UserResponse;
import dev.hgjtu.auth_client.services.CommunicationService;
import dev.hgjtu.auth_client.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/communication")
public class CommunicationController {
    private final UserService userService;
    private final CommunicationService communicationService;

    @GetMapping
    public Mono<String> communicationHome(Model model,
                       @RequestParam(defaultValue = "1") Short sectionId,
                       @RequestParam(defaultValue = "1") Short categoryId,
                       @AuthenticationPrincipal OAuth2User principal) {
        Mono<UserResponse> userMono = userService.getUserInfoByUsername(principal.getAttribute("sub"));
        Mono<List<SectionResponse>> sectionsMono = communicationService.getAllSections().collectList();
        Mono<List<CategoryResponse>> categoriesMono = communicationService.getCategoriesBySectionId(sectionId).collectList();
        Mono<List<SmileyReactionResponse>> smileyReactionsMono = communicationService.getAllSmileyReactions().collectList();
        Mono<List<PostResponse>> postsMono = communicationService.getPostsBySectionIdAndCategoryId(sectionId, categoryId).collectList();

        return Mono.zip(userMono, sectionsMono, categoriesMono, smileyReactionsMono, postsMono)
                .map(tuple -> {
                    model.addAttribute("user", tuple.getT1());
                    model.addAttribute("sections", tuple.getT2());
                    model.addAttribute("categories", tuple.getT3());
                    model.addAttribute("smileyReactions", tuple.getT4());
                    model.addAttribute("posts", tuple.getT5());
                    return "communication/main-page";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

}
