package dev.hgjtu.auth_client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemRequest {
    private String title;
    private Integer categoryId;
    private String description;
    private List<String> imagesUrls;
    private Integer price;
    private String location;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
