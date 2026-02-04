package com.myProject.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class PostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

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
