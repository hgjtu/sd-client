package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.communication.*;
import dev.hgjtu.auth_client.dto.user.UserResponse;
import dev.hgjtu.auth_client.services.CommunicationService;
import dev.hgjtu.auth_client.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/communication")
public class CommunicationController {
    private final UserService userService;
    private final CommunicationService communicationService;

    @GetMapping
    public Mono<String> communicationHome(Model model,
                       @RequestParam(defaultValue = "1") Short sectionId,
                       @RequestParam(required = false) Short categoryId,
                       @AuthenticationPrincipal OAuth2User principal) {
        Mono<UserResponse> userMono = userService.getUserInfoByUsername(principal.getAttribute("sub"));
        Mono<List<SectionResponse>> sectionsMono = communicationService.getAllSections().collectList();
        Mono<List<CategoryResponse>> categoriesMono = communicationService.getCategoriesBySectionId(sectionId).collectList();
//        Mono<List<SmileyReactionResponse>> smileyReactionsMono = communicationService.getAllSmileyReactions().collectList();
        Mono<List<PostResponse>> postsMono = communicationService.getPostsBySectionIdAndCategoryId(sectionId, categoryId).collectList();
        Mono<List<PostResponse>> myPostsMono = communicationService.getPostsByUser().collectList();

        return Mono.zip(userMono, sectionsMono, categoriesMono, postsMono, myPostsMono)
                .map(tuple -> {
                    model.addAttribute("user", tuple.getT1());
                    model.addAttribute("sections", tuple.getT2());
                    model.addAttribute("categories", tuple.getT3());
                    model.addAttribute("posts", tuple.getT4());
                    model.addAttribute("myPosts", tuple.getT5());
                    return "communication/main-page";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @GetMapping("/post/{id}")
    public Mono<String> postPage(Model model, @PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        Mono<UserResponse> userMono = userService.getUserInfoByUsername(principal.getAttribute("sub"));
        Mono<PostResponse> postMono = communicationService.getPostById(id);
        return Mono.zip(userMono, postMono)
                .map(tuple -> {
                    model.addAttribute("user", tuple.getT1());
                    model.addAttribute("post", tuple.getT2());
                    return "communication/post-page";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }


    @GetMapping("/post/create")
    public Mono<String> createPost (Model model) {
        Flux<SectionResponse> sectionsMono = communicationService.getAllSections();
        Mono<List<CategoryResponse>> categoriesMono = communicationService.getAllCategories().collectList();

        return Mono.zip(sectionsMono.collectList(), categoriesMono)
                .map(tuple -> {
                    model.addAttribute("sections", tuple.getT1());
                    model.addAttribute("categories", tuple.getT2());
                    model.addAttribute("actionUrl", "/communication/post/create");

                    return "communication/create-post";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @PostMapping("/post/create")
    @ResponseBody
    public Mono<Long> createPost (@RequestBody PostRequest postRequest) {
        return communicationService.createPost(postRequest);
    }

    @GetMapping("/post/edit/{id}")
    public Mono<String> editPostById(Model model,
                                     @PathVariable Long id){
        Mono<List<SectionResponse>> sectionsMono = communicationService.getAllSections().collectList();
        Mono<List<CategoryResponse>> categoriesMono = communicationService.getAllCategories().collectList();
        Mono<PostResponse> postMono = communicationService.getPostById(id);

        return Mono.zip(sectionsMono, categoriesMono, postMono)
                .map(tuple -> {
                    model.addAttribute("sections", tuple.getT1());
                    model.addAttribute("categories", tuple.getT2());
                    model.addAttribute("post", tuple.getT3());
                    model.addAttribute("actionUrl", "/communication/post/edit/" + id);

                    return "communication/create-post";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @PostMapping("/post/edit/{id}")
    @ResponseBody
    public Mono<Long> editPost (@RequestBody PostRequest postRequest,
                                  @PathVariable Long id) {
        return communicationService.updatePost(id, postRequest);
    }

    @PostMapping("/post/add-media/{postId}")
    @ResponseBody
    public Mono<Void> attachMediaToItem(@PathVariable Long postId,
                                        @RequestBody List<UUID> mediaIds) {
        return communicationService.attachMediaToPost(postId, mediaIds);
    }

    @DeleteMapping("/post/delete/{id}")
    public Mono<String> deleteItemById (@PathVariable Long id) {
        return communicationService.deletePost(id)
                .then(Mono.just("redirect:/communication"));
    }

    @DeleteMapping("/post/delete-media/{postId}")
    @ResponseBody
    public Mono<Void> deleteItemById (@PathVariable Long postId,
                                        @RequestBody List<UUID> mediaIds) {
        return communicationService.deleteMediaToPost(postId, mediaIds);
    }

    @GetMapping("/post/{postId}/add-reaction/{reactionType}")
    @ResponseBody
    public Mono<Void> deleteItemById (@PathVariable Long postId, @PathVariable String reactionType) {
        return communicationService.addReaction(postId, reactionType)
                .then(Mono.empty());
    }

    @PostMapping("/post/comment/add")
    @ResponseBody
    public Mono<Void> addCommentToPost (@RequestBody CommentRequest commentRequest) {
        return communicationService.addComment(commentRequest)
                .then(Mono.empty());
    }

    @PostMapping("/post/comment/edit/{id}")
    @ResponseBody
    public Mono<Void> editCommentById (@PathVariable Long id,
                                      @RequestBody  CommentRequest commentRequest) {
        return communicationService.editComment(id, commentRequest)
                .then(Mono.empty());
    }

    @PostMapping("/post/comment/delete/{id}")
    @ResponseBody
    public Mono<Void> deleteCommentById (@PathVariable Long id) {
        return communicationService.deleteComment(id)
                .then(Mono.empty());
    }
}
