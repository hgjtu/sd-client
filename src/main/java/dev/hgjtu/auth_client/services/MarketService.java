package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.market.CategoryResponse;
import dev.hgjtu.auth_client.dto.market.ItemMinResponse;
import dev.hgjtu.auth_client.dto.market.ItemRequest;
import dev.hgjtu.auth_client.dto.market.ItemResponse;
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
    private final WebClient serverWebClient;

    @Value("${MARKET_RESOURCE_SERVER_URL}")
    private String gatewayServiceURL;
    @Value("${MARKET_RESOURCE_PREFIX}")
    private String marketResourcePrefix;

    public Flux<CategoryResponse> getAllCategories() {
        return serverWebClient.get()
                .uri(gatewayServiceURL + marketResourcePrefix + "/categories")
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
                .uri(gatewayServiceURL + marketResourcePrefix + "/categories/{name}", name)
                .retrieve()
                .bodyToMono(CategoryResponse.class);
    }

    public Mono<String> getCategoryNameById(Integer id) {
        return webClient.get()
                .uri(gatewayServiceURL + marketResourcePrefix + "/categories-name/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Flux<ItemMinResponse> getAllItemsByCategoryAndMode(String category, String mode) {
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        params.put("mode", mode);
        return webClient.get()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/all-by-category/{category}/{mode}", params)
                .retrieve()
                .bodyToFlux(ItemMinResponse.class);
    }

    public Mono<ItemResponse> addItem(ItemRequest itemRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Mono<ItemResponse> editItem(Long id, ItemRequest itemRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/edit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Mono<String> deleteItem(Long id){
        return webClient.delete()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/delete/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<ItemResponse> getItemById(Long id) {
        return webClient.get()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/{id}", id)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Flux<ItemMinResponse> getUserItemsAndRequests(String username, String mode) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("mode", mode);
        return webClient.get()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/all-by-username/{username}/{mode}", params)
                .retrieve()
                .bodyToFlux(ItemMinResponse.class);
    }

}
