package dev.hgjtu.auth_client.services;

import dev.hgjtu.auth_client.dto.PageResponse;
import dev.hgjtu.auth_client.dto.jump_group.GroupRequest;
import dev.hgjtu.auth_client.dto.jump_group.GroupResponse;
import dev.hgjtu.auth_client.dto.jump_group.SearchRequest;
import dev.hgjtu.auth_client.dto.jump_group.TrainingLevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JumpGroupService {
    private final WebClient webClient;

    @Value("${GROUP_RESOURCE_SERVER_URL}")// GATEWAY_SERVICE_URL
    private String gatewayServiceURL;
    @Value("${JUMP_GROUP_RESOURCE_PREFIX}")
    private String jumpGroupResourcePrefix;

    public Mono<PageResponse<GroupResponse>> getGroupsByParams(Integer page,
                                                       Integer size,
                                                       String sort,
                                                       String direction,
                                                       List<String> places,
                                                       Short trainingLevel,
                                                       LocalDateTime jumpDateTimeStart,
                                                       LocalDateTime jumpDateTimeEnd,
                                                       Boolean isParticipant) {
        SearchRequest searchRequest = new SearchRequest();

        // Добавляем параметры если они не null
        if (page != null) {
            searchRequest.setPage(page);
        }
        if (size != null) {
            searchRequest.setSize(size);
        }
        if (sort != null) {
            searchRequest.setSort(sort);
        }
        if (direction != null) {
            searchRequest.setDirection(direction);
        }
        if(places != null) {
            searchRequest.setPlacesList(places);
        }
        if (trainingLevel != null) {
            searchRequest.setTrainingLevel(trainingLevel);
        }
        if (jumpDateTimeStart != null) {
           searchRequest.setJumpDateTimeStart(jumpDateTimeStart);
        }
        if (jumpDateTimeEnd != null) {
            searchRequest.setJumpDateTimeEnd(jumpDateTimeEnd);
        }
        if (isParticipant != null) {
            searchRequest.setParticipant(isParticipant);
        }

        // Для списка добавляем каждый элемент отдельно
//        if (places != null && !places.isEmpty()) {
//            places.forEach(place -> uriBuilder.queryParam("places", place));
//        }

        return webClient.post()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(searchRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<GroupResponse>>() {});
    }

    public Mono<GroupResponse> getGroupById(Integer id) {
        return webClient.get()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/{id}", id)
                .retrieve()
                .bodyToMono(GroupResponse.class);
    }

    public Flux<TrainingLevelResponse> getTrainingLevels() {
        return webClient.get()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/training-levels")
                .retrieve()
                .bodyToFlux(TrainingLevelResponse.class);
    }


    public Mono<GroupResponse> createGroup(GroupRequest groupRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(groupRequest)
                .retrieve()
                .bodyToMono(GroupResponse.class);
    }


    public Mono<GroupResponse> editGroup(Integer id, GroupRequest groupRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/edit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(groupRequest)
                .retrieve()
                .bodyToMono(GroupResponse.class);
    }

    public Mono<String> deleteGroup(Integer groupId) {
        return webClient.delete()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/{id}", groupId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<GroupResponse> addComment(dev.hgjtu.auth_client.dto.jump_group.CommentRequest commentRequest) {
        return webClient.post()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/add-comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequest)
                .retrieve()
                .bodyToMono(GroupResponse.class);
    }

    public Mono<GroupResponse> editComment(Long commentId, dev.hgjtu.auth_client.dto.jump_group.CommentRequest commentRequest) {
        return webClient.patch()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/edit-comment/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequest)
                .retrieve()
                .bodyToMono(GroupResponse.class);
    }

    public Mono<String> deleteComment(Long commentId) {
        return webClient.delete()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/delete-comment/{commentId}", commentId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> joinGroup(Integer groupId) {
        return webClient.post()
                .uri(gatewayServiceURL + jumpGroupResourcePrefix + "/groups/join-group/{groupId}", groupId)
                .retrieve()
                .bodyToMono(String.class);
    }

}
