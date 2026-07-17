package com.example.apilab.repository.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String message;
    private T data;
    private String path;

    public static <T> ApiResponse<T> success(int status, String message, T data, String path) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .status(status)
                .message(message)
                .data(data)
                .path(path)
                .build();
    }
}
