package dev.hgjtu.auth_client.dto.market;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Integer id;
    private String name;
    private String nameRu;
    private String shortDescription;
    private String description;
    private String categoryMediaURL;
}
