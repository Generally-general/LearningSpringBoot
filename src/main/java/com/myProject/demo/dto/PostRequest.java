package com.myProject.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request object representing a post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank
    @Schema(description = "Title of the post")
    private String title;

    @NotBlank
    @Schema(description = "Content of the post")
    private String content;

    @Schema(description = "User's ID")
    private Integer userId;

    @Schema(description = "Version of the post")
    private Long version;
}
