package com.myProject.demo.controller;

import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostByPostId(@PathVariable Integer postId) {
        PostResponse post = postService.getPostByIdOrThrow(postId);
        return ResponseEntity.ok(post);
    }
}
