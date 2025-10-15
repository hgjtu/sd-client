package dev.hgjtu.auth_client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class JumpRequest {
    private String userName;
    private LocalDate date;
    private String location;
    private String type;
    private Integer altitude;
    private String notes;
}
