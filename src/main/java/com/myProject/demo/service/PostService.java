package com.myProject.demo.service;

import com.myProject.demo.dto.PostRequest;
import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.entity.Post;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.ResourceNotFoundException;
import com.myProject.demo.repository.PostRepository;
import com.myProject.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostResponse> getPostsByUserOrThrow(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + userId
                ));
        List<Post> posts = postRepository.findByUser(user);

        return posts.stream()
                .map(this::toResponse)
                .toList();
    }

    public PostResponse getPostByIdOrThrow(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));

        return toResponse(post);
    }

    public PostResponse createPost(Integer userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + userId
                ));
        Post post = new Post();
        post.setUser(user);

        return mapAndSavePost(request, post);
    }

    private PostResponse mapAndSavePost(PostRequest request, Post existingPost) {
        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());

        if(existingPost.getId() == null) {
            existingPost.setCreatedAt(LocalDateTime.now());
        }

        Post saved = postRepository.save(existingPost);
        return toResponse(saved);
    }

    public PostResponse toResponse(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}
