package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.MediaUploadResponse;
import dev.hgjtu.auth_client.dto.UploadUrlRequest;
import dev.hgjtu.auth_client.dto.market.CommentRequest;
import dev.hgjtu.auth_client.dto.market.CategoryResponse;
import dev.hgjtu.auth_client.dto.market.ItemMinResponse;
import dev.hgjtu.auth_client.dto.market.ItemRequest;
import dev.hgjtu.auth_client.dto.market.ItemResponse;
import dev.hgjtu.auth_client.models.AvailableResources;
import dev.hgjtu.auth_client.models.PostComment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.util.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.InputStream;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class MarketService {
    private final WebClient webClient;
    private final WebClient serverWebClient;

    private final MediaService mediaService;

    @Value("${GATEWAY_SERVICE_URL}")
    private String gatewayServiceURL;
    @Value("${MARKET_RESOURCE_PREFIX}")
    private String marketResourcePrefix;

    public Flux<CategoryResponse> getAllCategories() {
        return serverWebClient.get()
                .uri(gatewayServiceURL + marketResourcePrefix + "/categories")
                .retrieve()
                .bodyToFlux(CategoryResponse.class)
                .sort(Comparator.comparing(CategoryResponse::getId));
    }

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

    public Mono<Void> attachMediaToItem(Long itemId, List<UUID> mediaIds) {
        if (mediaIds.isEmpty()) {
            return Mono.empty();
        }

        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/add-media/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mediaIds)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Long> addItem(ItemRequest itemRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<Long> editItem(Long id, ItemRequest itemRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/edit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<String> deleteItem(Long id){
        return webClient.delete()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/delete/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Void> deleteMediaToItem(Long itemId, UUID mediaId){
        Map<String, String> params = new HashMap<>();
        params.put("itemId", itemId.toString());
        params.put("mediaId", mediaId.toString());
        return webClient.delete()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/delete-media/{itemId}/{mediaId}", params)
                .retrieve()
                .bodyToMono(Void.class);
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

    public Mono<PostComment> addComment(CommentRequest commentRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/add-comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequest)
                .retrieve()
                .bodyToMono(PostComment.class);
    }

    public Mono<ItemResponse> editComment(Long commentId, CommentRequest commentRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/edit-comment/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequest)
                .retrieve()
                .bodyToMono(ItemResponse.class);
    }

    public Mono<String> deleteComment(Long commentId) {
        return webClient.delete()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/delete-comment/{commentId}", commentId)
                .retrieve()
                .bodyToMono(String.class);
    }

}
