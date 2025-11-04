package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.communication.*;
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

public class CommunicationService {
    private final WebClient webClient;

    @Value("${COMMUNICATION_RESOURCE_SERVER_URL}")
    private String gatewayServiceURL;
    @Value("${COMMUNICATION_RESOURCE_PREFIX}")
    private String communicationResourcePrefix;

    public Flux<SectionResponse> getAllSections() {
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/sections")
                .retrieve()
                .bodyToFlux(SectionResponse.class);
    }

    public Flux<CategoryResponse> getAllCategories() {
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/categories")
                .retrieve()
                .bodyToFlux(CategoryResponse.class);
    }

    public Flux<CategoryResponse> getCategoriesBySectionId(Short sectionId) {
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/sections/{id}/category", sectionId)
                .retrieve()
                .bodyToFlux(CategoryResponse.class);
    }

    public Flux<SmileyReactionResponse> getAllSmileyReactions(){
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/smiley-reactions")
                .retrieve()
                .bodyToFlux(SmileyReactionResponse.class);
    }

    public Flux<PostResponse> getPostsBySectionIdAndCategoryId(Short sectionId, Short categoryId) {
        Map<String, Short> params = new HashMap<>();
        params.put("sectionId", sectionId);
        params.put("categoryId", categoryId);
        if(categoryId != null) return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/section/{sectionId}/category/{categoryId}", params)
                .retrieve()
                .bodyToFlux(PostResponse.class);
        else return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/section/{sectionId}", sectionId)
                .retrieve()
                .bodyToFlux(PostResponse.class);
    }

    public Flux<PostResponse> getPostsByUser(){
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/user")
                .retrieve()
                .bodyToFlux(PostResponse.class);
    }

    public Mono<PostResponse> getPostById(Long postId) {
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/{id}", postId)
                .retrieve()
                .bodyToMono(PostResponse.class);
    }

    public Mono<PostResponse> createPost(PostRequest postRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postRequest)
                .retrieve()
                .bodyToMono(PostResponse.class);
    }

    public Mono<PostResponse> updatePost(Long postId, PostRequest postRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postRequest)
                .retrieve()
                .bodyToMono(PostResponse.class);
    }

    public Mono<String> deletePost(Long postId) {
        return webClient.delete()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/{id}", postId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Void> addReaction(Long postId, Short reactionId) {
        Map<String, Long> params = new HashMap<>();
        params.put("postId", postId);
        params.put("reactionId", Long.valueOf(reactionId));
        return webClient.get()
                .uri(gatewayServiceURL + communicationResourcePrefix + "/posts/{postId}/add-reaction/{reactionId}", params)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
