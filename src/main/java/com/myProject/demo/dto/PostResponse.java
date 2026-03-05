package com.myProject.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Response object representing a post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    @Schema(description = "Unique post ID")
    private Integer id;

    @Schema(description = "Title of the post")
    private String title;

    @Schema(description = "Content of the post")
    private String content;

    @Schema(description = "Time of creation of the post")
    private LocalDateTime createdAt;

    @Schema(description = "Version of the post")
    private Long version;
}
