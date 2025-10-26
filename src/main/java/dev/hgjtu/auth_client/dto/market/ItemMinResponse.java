package dev.hgjtu.auth_client.dto.market;

public class ItemMinResponse {
    private Long id;
    private String title;
    private String mainImageUrl;
    private Integer price;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public Integer getPrice() {
        return price;
    }
}
