package com.myProject.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object representing a post")
public class PostRequest {
    @NotBlank
    @Schema(description = "Title of the post")
    private String title;

    @NotBlank
    @Schema(description = "Content of the post")
    private String content;

    @Schema(description = "User's ID")
    private Integer userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
