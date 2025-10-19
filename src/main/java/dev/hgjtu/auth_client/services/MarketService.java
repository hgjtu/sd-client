package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.CategoryResponse;
import dev.hgjtu.auth_client.dto.ItemMinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<ItemMinResponse> getAllItemsByCategory(String category) {
        return webClient.get()
                .uri(marketResourceServerUrl + "/api/items/all-by-category/{category}", category)
                .retrieve()
                .bodyToFlux(ItemMinResponse.class);
    }

}
