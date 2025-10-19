package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.CategoryResponse;
import dev.hgjtu.auth_client.dto.ItemMinResponse;
import dev.hgjtu.auth_client.dto.ItemRequest;
import dev.hgjtu.auth_client.dto.user.JumpRequest;
import dev.hgjtu.auth_client.services.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {
    private final MarketService marketService;

    @GetMapping
    public Mono<String> market(Model model) {
        return marketService.getAllCategories()
                .collectList()
                .map(categories -> {
                    model.addAttribute("categories", categories);
                    return "market/market-dashboard";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/market-dashboard");
                });
    }

    @GetMapping("/category/{slug}")
    public Mono<String> category(@PathVariable String slug,
                                 @RequestParam(defaultValue = "buy") String mode,
                                 Model model) {
        Mono<CategoryResponse> categoryMono = marketService.getCategoryByName(slug);
        Mono<List<ItemMinResponse>> itemsMono = marketService.getAllItemsByCategoryAndMode(slug, mode).collectList();

        return Mono.zip(categoryMono, itemsMono)
                .map(tuple -> {
                    CategoryResponse category = tuple.getT1();
                    List<ItemMinResponse> items = tuple.getT2();

                    model.addAttribute("categoryName", category.getNameRu());
                    model.addAttribute("categoryDescription", category.getDescription());
                    model.addAttribute("mode", mode);
                    model.addAttribute("items", items);

                    return "market/category";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/category");
                });
    }

    @GetMapping("/add-item")
    public Mono<String> addItem(Model model) {
        return marketService.getAllCategories()
                .collectList()
                .map(categories -> {
                    model.addAttribute("categories", categories);
                    return "market/add-item";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/add-item");
                });
    }

    @PostMapping("/add-item")
    public Mono<String> addItem (@ModelAttribute ItemRequest itemRequest) {
        itemRequest.setType("buy");
        return marketService.addItem(itemRequest)
                .then(Mono.just("redirect:/market"));
    }

    @GetMapping("/add-request")
    public Mono<String> addRequest(Model model) {
        return marketService.getAllCategories()
                .collectList()
                .map(categories -> {
                    model.addAttribute("categories", categories);
                    return "market/add-request";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/add-request");
                });
    }

    @PostMapping("/add-request")
    public Mono<String> addRequest (@ModelAttribute ItemRequest itemRequest) {
        itemRequest.setType("sell");
        itemRequest.setPrice(0);
        return marketService.addItem(itemRequest)
                .then(Mono.just("redirect:/market"));
    }

    @GetMapping("/item/{id}")
    public Mono<String> getItemById(Model model,
                                    @PathVariable Long id) {
        return marketService.getItemById(id)
                .map(item -> {
                    model.addAttribute("item", item);
                    return "market/item";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/item");
                });
    }
}
