package com.myProject.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response object of API")
public class ApiResponse<T> {
    @Schema(description = "Success flag")
    private boolean success;
    @Schema(description = "Message component")
    private String message;
    @Schema(description = "Data of the Response")
    private T data;
    @Schema(description = "Timestamp")
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
