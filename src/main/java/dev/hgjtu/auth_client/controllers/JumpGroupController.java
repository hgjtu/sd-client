package dev.hgjtu.auth_client.controllers;


import dev.hgjtu.auth_client.dto.PageResponse;
import dev.hgjtu.auth_client.dto.communication.PostResponse;
import dev.hgjtu.auth_client.dto.jump_group.GroupRequest;
import dev.hgjtu.auth_client.dto.jump_group.GroupResponse;
import dev.hgjtu.auth_client.dto.jump_group.TrainingLevelResponse;
import dev.hgjtu.auth_client.dto.user.UserResponse;
import dev.hgjtu.auth_client.services.JumpGroupService;
import dev.hgjtu.auth_client.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jump-group")
public class JumpGroupController {
    private final UserService userService;
    private final JumpGroupService jumpGroupService;

    @GetMapping
    public Mono<String> jumpGroupHome(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(defaultValue = "jumpDateTime") String sort,
                                      @RequestParam(defaultValue = "DESC") String direction,
                                      @RequestParam(required = false) List<String> places,
                                      @RequestParam(required = false) Short trainingLevel,
                                      @RequestParam(required = false) LocalDateTime jumpDateTimeStart,
                                      @RequestParam(required = false) LocalDateTime jumpDateTimeEnd,
                                      @RequestParam(required = false, defaultValue = "false") Boolean isParticipant,
                                      Model model) {

        Mono<List<TrainingLevelResponse>> trainingLevels = jumpGroupService.getTrainingLevels().collectList();
        Mono<PageResponse<GroupResponse>> groupsPage = jumpGroupService.getGroupsByParams(
                page, size, sort, direction, places, trainingLevel,
                jumpDateTimeStart, jumpDateTimeEnd, isParticipant
        );
        return Mono.zip(trainingLevels, groupsPage)
                .map(tuple -> {
                    model.addAttribute("trainingLevels", tuple.getT1());
                    model.addAttribute("groupsPage", tuple.getT2());
                    return "groups/find-group";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @GetMapping("/{id}")
    public Mono<String> groupPage(Model model, @PathVariable Integer id, @AuthenticationPrincipal OAuth2User principal) {
        Mono<UserResponse> userMono = userService.getUserInfoByUsername(principal.getAttribute("sub"));
        Mono<GroupResponse> groupMono = jumpGroupService.getGroupById(id);
        return Mono.zip(userMono, groupMono)
                .map(tuple -> {
                    model.addAttribute("user", tuple.getT1());
                    model.addAttribute("group", tuple.getT2());
                    return "groups/group-page";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @GetMapping("/create-group")
    public Mono<String> createGroup(Model model) {
        return jumpGroupService.getTrainingLevels()
                .collectList()
                .map(levels -> {
                    model.addAttribute("trainingLevels", levels);
                    model.addAttribute("actionUrl", "/jump-group/create-group");
                    return "groups/create-group";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @PostMapping("/create-group")
    public Mono<String> createGroup (@ModelAttribute GroupRequest groupRequest) {
        return jumpGroupService.createGroup(groupRequest)
                .then(Mono.just("redirect:/jump-group"));
    }

    @GetMapping("/edit/{id}")
    public Mono<String> editGroupById(Model model,
                                        @PathVariable Integer id) {
        Mono<List<TrainingLevelResponse>> trainingLevelsMono = jumpGroupService.getTrainingLevels().collectList();
        Mono<GroupResponse> groupMono = jumpGroupService.getGroupById(id);

        return Mono.zip(trainingLevelsMono, groupMono)
                .map(tuple -> {
                    List<TrainingLevelResponse> trainingLevels = tuple.getT1();
                    GroupResponse group = tuple.getT2();

                    model.addAttribute("trainingLevels", trainingLevels);
                    model.addAttribute("group", group);
                    model.addAttribute("actionUrl", "/jump-group/edit/" + group.getId());
                    return "groups/create-group";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @PostMapping("/edit/{id}")  //TODO  во фронте тоже где-то надо настроить что только владелец редактирует нигде вроде такой проверки нет
    public Mono<String> editGroupById (@PathVariable Integer id,
                                      @ModelAttribute GroupRequest groupRequest) {
        return jumpGroupService.editGroup(id, groupRequest)
                .then(Mono.just("redirect:/jump-group/{id}"));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> deleteItemById (@PathVariable Integer id) {
        return jumpGroupService.deleteGroup(id)
                .then(Mono.just("redirect:/jump-group"));
    }

    @PostMapping("/comment/add")
    @ResponseBody
    public Mono<Void> addCommentToGroup (@RequestBody dev.hgjtu.auth_client.dto.jump_group.CommentRequest commentRequest) {
        return jumpGroupService.addComment(commentRequest)
                .then(Mono.empty());
    }

    @PostMapping("/comment/edit/{id}")
    @ResponseBody
    public Mono<Void> editCommentById (@PathVariable Long id,
                                       @RequestBody  dev.hgjtu.auth_client.dto.jump_group.CommentRequest commentRequest) {
        return jumpGroupService.editComment(id, commentRequest)
                .then(Mono.empty());
    }

    @PostMapping("/comment/delete/{id}")
    @ResponseBody
    public Mono<Void> deleteCommentById (@PathVariable Long id) {
        return jumpGroupService.deleteComment(id)
                .then(Mono.empty());
    }

    @PostMapping("/join-group/{id}")
    public Mono<String> joinGroup (@PathVariable Integer id) {
        return jumpGroupService.joinGroup(id)
                .then(Mono.just("redirect:/jump-group/{id}"));
    }

}
