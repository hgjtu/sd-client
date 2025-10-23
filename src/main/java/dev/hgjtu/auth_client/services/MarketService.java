package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.CategoryResponse;
import dev.hgjtu.auth_client.dto.ItemMinResponse;
import dev.hgjtu.auth_client.dto.ItemRequest;
import dev.hgjtu.auth_client.dto.ItemResponse;
import dev.hgjtu.auth_client.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketService {
    private final WebClient webClient;


    @Value("${MARKET_RESOURCE_SERVER_URL}")
    private String marketResourceServerUrl;

    public Flux<CategoryResponse> getAllCategories() {
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/categories")
                .retrieve()
                .bodyToFlux(CategoryResponse.class);
    }

//    public Mono<CategoryResponse> getCategoryById(Integer id) {
//        return webClient.get()
//                .uri(marketResourceServerUrl + "/api/categories/{id}", id)
//                .retrieve()
//                .bodyToMono(CategoryResponse.class);
//    }

    public Mono<CategoryResponse> getCategoryByName(String name) {
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/categories/{name}", name)
                .retrieve()
                .bodyToMono(CategoryResponse.class);
    }

    public Mono<String> getCategoryNameById(Integer id) {
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/categories-name/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Flux<ItemMinResponse> getAllItemsByCategoryAndMode(String category, String mode) {
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        params.put("mode", mode);
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/items/all-by-category/{category}/{mode}", params)
                .retrieve()
                .bodyToFlux(ItemMinResponse.class);
    }

    public Mono<ItemResponse> addItem(ItemRequest itemRequest) {
        return webClient.post()
                .uri(marketResourceServerUrl + "/api/items/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Mono<ItemResponse> editItem(Long id, ItemRequest itemRequest) {
        return webClient.patch()
                .uri(marketResourceServerUrl + "/api/items/edit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Mono<String> deleteItem(Long id){
        return webClient.delete()
                .uri(marketResourceServerUrl + "/api/items/delete/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<ItemResponse> getItemById(Long id) {
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/items/{id}", id)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Flux<ItemMinResponse> getUserItemsAndRequests(String username, String mode) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("mode", mode);
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/items/all-by-username/{username}/{mode}", params)
                .retrieve()
                .bodyToFlux(ItemMinResponse.class);
    }

}
