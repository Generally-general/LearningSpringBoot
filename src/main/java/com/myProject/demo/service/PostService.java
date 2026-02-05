package com.myProject.demo.service;

import com.myProject.demo.dto.PostRequest;
import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.entity.Post;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.ConflictException;
import com.myProject.demo.exception.ResourceNotFoundException;
import com.myProject.demo.repository.PostRepository;
import com.myProject.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<PostResponse> getPosts(
            Pageable pageable
    ) {
        return postRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public Page<PostResponse> getPostsByUserOrThrow(Integer userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id " + userId
                ));
        Page<Post> posts = postRepository.findByUser(user, pageable);

        return posts.map(this::toResponse);
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

    public PostResponse updatePostOrThrow(Integer userId, Integer postId, PostRequest request) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));
        if(!existingPost.getUser().getId().equals(userId)) {
            throw new ConflictException("Post does not belong to this user");
        }
        return mapAndSavePost(request, existingPost);
    }

    public void deletePostOrThrow(Integer userId, Integer postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));
        if(!existingPost.getUser().getId().equals(userId)) {
            throw new ConflictException("Post does not belong to this user");
        }
        postRepository.delete(existingPost);
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
