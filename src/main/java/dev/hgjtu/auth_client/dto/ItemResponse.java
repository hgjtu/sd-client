package dev.hgjtu.auth_client.dto;

import dev.hgjtu.auth_client.models.Comment;
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
    private List<String> imagesUrls;
    private Integer price;
    private String location;
    private LocalDate publicationDate;
    private String type;
    private List<Comment> comments;
}
