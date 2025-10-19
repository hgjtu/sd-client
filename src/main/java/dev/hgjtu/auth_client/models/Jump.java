package dev.hgjtu.auth_client.models;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Jump {
    private Long id;
    private Long userId;
    private LocalDate date;
    private String location;
    private String type;
    private Integer altitude;
    private String notes;
}
