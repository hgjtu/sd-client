package dev.hgjtu.auth_client.dto.market;

import dev.hgjtu.auth_client.dto.MediaUploadResponse;
import dev.hgjtu.auth_client.models.ItemComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private Integer categoryId;
    private String title;
    private String description;
    private String username;
    private List<MediaUploadResponse> medias;
    private Integer price;
    private String location;
    private LocalDate publicationDate;
    private String type;
    private List<ItemComment> comments;
}
