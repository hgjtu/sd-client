package dev.hgjtu.auth_client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemRequest {
    private String title;
    private String description;
    private List<String> imagesUrls;
    private Integer price;
    private String location;
}
