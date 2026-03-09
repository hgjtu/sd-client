package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.MediaUploadResponse;
import dev.hgjtu.auth_client.dto.UploadUrlRequest;
import dev.hgjtu.auth_client.dto.market.CategoryResponse;
import dev.hgjtu.auth_client.dto.market.ItemMinResponse;
import dev.hgjtu.auth_client.dto.market.ItemRequest;
import dev.hgjtu.auth_client.dto.market.ItemResponse;
import dev.hgjtu.auth_client.models.AvailableResources;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.util.*;

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

    public Mono<Long> addItemWithMediaFiles(ItemRequest itemRequest, List<MultipartFile> mediaFiles) {
        Mono<Long> qw =  addItem(itemRequest);
        processMediaFiles(qw.block(), mediaFiles).subscribe();
        return qw;
    }

    private Mono<Long> processMediaFiles(Long itemId, List<MultipartFile> mediaFiles) {
        if (mediaFiles == null || mediaFiles.isEmpty()) {
            return Mono.just(itemId);
        }

        return Flux.fromIterable(mediaFiles)
                .flatMap(media -> processSingleMediaFile(itemId, media))
                .collectList()
                .flatMap(mediaIds -> {
                    if (mediaIds.isEmpty()) {
                        return Mono.just(itemId);
                    }
                    return attachMediaToItem(itemId, mediaIds)
                            .thenReturn(itemId);
                });
    }

    private Mono<UUID> processSingleMediaFile(Long itemId, MultipartFile fileInfo) {
        return getUploadUrlForItem(AvailableResources.MARKET,
                        new UploadUrlRequest(fileInfo.getName(), fileInfo.getContentType()), itemId)
                .flatMap(uploadResponse -> {
                    UUID mediaId = uploadResponse.getId();
                    String uploadUrl = uploadResponse.getUploadUrl();

                    return uploadFileToS3(uploadUrl, fileInfo)
                            .then(mediaService.completeMedia(AvailableResources.MARKET, mediaId))
                            .thenReturn(mediaId);
                });
    }

    public Mono<MediaUploadResponse> getUploadUrlForItem(AvailableResources resourceName,
                                                         UploadUrlRequest uploadUrlRequest,
                                                         Long itemId){
        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/media/upload-url/item/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(uploadUrlRequest)
                .retrieve()
                .bodyToMono(MediaUploadResponse.class);
    }

    private Mono<Void> uploadFileToS3(String uploadUrl, MultipartFile fileInfo) {
        return Mono.fromCallable(() -> {
                    try {
                        return fileInfo.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get file input stream", e);
                    }
                })
                .flatMapMany(inputStream ->
                        DataBufferUtils.read((Path) inputStream,
                                new DefaultDataBufferFactory(),
                                4096))
                .as(dataBufferFlux ->
                        webClient.put()
                                .uri(uploadUrl)
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .contentLength(fileInfo.getSize())
                                .body(BodyInserters.fromDataBuffers(dataBufferFlux))
                                .retrieve()
                                .bodyToMono(Void.class)
                );
    }

    public Mono<Long> attachMediaToItem(Long itemId, List<UUID> mediaIds) {
        if (mediaIds.isEmpty()) {
            return Mono.just(itemId);
        }

        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items//add-media/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mediaIds)
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<Long> addItem(ItemRequest itemRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + marketResourcePrefix + "/items/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(itemRequest)
                .retrieve()
                .bodyToMono(Long.class);
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
