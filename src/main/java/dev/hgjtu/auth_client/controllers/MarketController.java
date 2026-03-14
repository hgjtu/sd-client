package dev.hgjtu.auth_client.controllers;

import dev.hgjtu.auth_client.dto.market.CategoryResponse;
import dev.hgjtu.auth_client.dto.market.ItemMinResponse;
import dev.hgjtu.auth_client.dto.market.ItemRequest;
import dev.hgjtu.auth_client.dto.market.ItemResponse;
import dev.hgjtu.auth_client.services.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
                    model.addAttribute("actionUrl", "/market/add-item");
                    return "market/add-item";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("error");
                });
    }

    @PostMapping(value = "/add-item")
    @ResponseBody
    public Mono<Long> addItem(@RequestBody ItemRequest itemRequest) {
        itemRequest.setType("buy");
        return marketService.addItem(itemRequest);
    }

    @GetMapping("/add-request")
    public Mono<String> addRequest(Model model) {
        return marketService.getAllCategories()
                .collectList()
                .map(categories -> {
                    model.addAttribute("categories", categories);
                    model.addAttribute("actionUrl", "/market/add-request");
                    return "market/add-request";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/add-request"); // TODO эта вообще про медиа ничего не знает
                });
    }
    @PostMapping("/add-request")
    @ResponseBody
    public Mono<Long> addRequest (@RequestBody ItemRequest itemRequest) {
        itemRequest.setType("sell");
        itemRequest.setPrice(0);
        return marketService.addItem(itemRequest);
    }

    @PostMapping("/items/add-media/{itemId}")
    @ResponseBody
    public Mono<Void> attachMediaToItem(@PathVariable Long itemId,
                                     @RequestBody List<UUID> mediaIds) {
        return marketService.attachMediaToItem(itemId, mediaIds);
    }

    @GetMapping("/my-items")
    public Mono<String> myItems(Model model,
                                @RequestParam(defaultValue = "buy") String mode,
                                @AuthenticationPrincipal OAuth2User principal) {
        return marketService.getUserItemsAndRequests(principal.getAttribute("sub"), mode.equals("buy") ? "sell" : "buy")
                .collectList()
                .map(items -> {
                    model.addAttribute("categoryName", "Мои товары");
                    model.addAttribute("categoryDescription", "Здесь отображаются ваши товары и заявки.");
                    model.addAttribute("mode", mode);
                    model.addAttribute("items", items);

                    return "market/category";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/category");
                });
    }

    @GetMapping("/item/{id}")
    public Mono<String> getItemById(Model model,
                                    @PathVariable Long id) {
        Mono<List<CategoryResponse>> categoriesMono = marketService.getAllCategories().collectList();
        Mono<ItemResponse> itemMono = marketService.getItemById(id);

        return Mono.zip(categoriesMono, itemMono)
                .map(tuple -> {
                    ItemResponse item = tuple.getT2();
                    String category = tuple.getT1().stream()
                            .filter(c -> c.getId().equals(item.getCategoryId()))
                            .map(CategoryResponse::getName)
                            .findFirst()
                            .orElse(null);;


                    model.addAttribute("category", category);
                    model.addAttribute("item", item);
                    return "market/item";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/item");
                });
    }

    @GetMapping("/item/edit/{id}")
    public Mono<String> editItemById(Model model,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal OAuth2User principal) {
        Mono<List<CategoryResponse>> categoriesMono = marketService.getAllCategories().collectList();
        Mono<ItemResponse> itemMono = marketService.getItemById(id);

        return Mono.zip(categoriesMono, itemMono)
                .map(tuple -> {
                    model.addAttribute("categories", tuple.getT1());
                    model.addAttribute("item", tuple.getT2());
                    model.addAttribute("actionUrl", "/market/item/edit/" + id);
                    return "market/add-item";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/add-item");
                });
    }

    @GetMapping("/request/edit/{id}")
    public Mono<String> editRequestById(Model model,
                                        @PathVariable Long id,
                                        @AuthenticationPrincipal OAuth2User principal) {
        Mono<List<CategoryResponse>> categoriesMono = marketService.getAllCategories().collectList();
        Mono<ItemResponse> itemMono = marketService.getItemById(id);

        return Mono.zip(categoriesMono, itemMono)
                .map(tuple -> {
                    model.addAttribute("categories", tuple.getT1());
                    model.addAttribute("item", tuple.getT2());
                    model.addAttribute("actionUrl", "/market/request/edit/" + tuple.getT2().getId());
                    return "market/add-request";
                })
                .onErrorResume(Exception.class, e -> {
                    model.addAttribute("error", "Ошибка при вызове API: " + e.getMessage());
                    return Mono.just("market/add-request");
                });
    }

    @PostMapping("/{type}/edit/{id}")
    @ResponseBody
    public Mono<Long> editItemById (@PathVariable String type, @PathVariable Long id,
                                      @ModelAttribute ItemRequest itemRequest) {

        if(Objects.equals(type, "item")){
            itemRequest.setType("buy");
        }
        else {
            itemRequest.setType("sell");
            itemRequest.setPrice(0);
        }
        return marketService.editItem(id, itemRequest);
    }

    @GetMapping("/item/delete/{id}")
    public Mono<String> deleteItemById (@PathVariable Long id) {
        return marketService.deleteItem(id)
                .then(Mono.just("redirect:/market"));
    }

    // TODO нет комментариев
}
