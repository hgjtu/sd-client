package dev.hgjtu.auth_client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Integer id;
    private String name;
    private String nameRu;
    private String shortDescription;
    private String description;
}
