package com.myProject.demo.service;

import com.myProject.demo.dto.PostRequest;
import com.myProject.demo.dto.PostResponse;
import com.myProject.demo.entity.Post;
import com.myProject.demo.entity.PostLike;
import com.myProject.demo.entity.User;
import com.myProject.demo.exception.ConflictException;
import com.myProject.demo.exception.ResourceNotFoundException;
import com.myProject.demo.repository.PostLikeRepository;
import com.myProject.demo.repository.PostRepository;
import com.myProject.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    public Page<PostResponse> getPosts(
            Pageable pageable
    ) {
        return postRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public Page<PostResponse> getPostsByUserOrThrow(User authenticatedUser, Pageable pageable) {
        if(authenticatedUser == null) {
            throw new AccessDeniedException("Unauthorised");
        }
        Page<Post> posts = postRepository.findByUser(authenticatedUser, pageable);

        return posts.map(this::toResponse);
    }

    public PostResponse getPostByIdOrThrow(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));

        return toResponse(post);
    }

    public PostResponse createPost(User authenticatedUser, PostRequest request) {
        if(authenticatedUser == null) {
            throw new AccessDeniedException("Unauthorised");
        }
        Post post = new Post();
        post.setUser(authenticatedUser);

        post.setCreatedAt(LocalDateTime.now());

        return mapAndSavePost(request, post);
    }

    public PostResponse updatePostOrThrow(
            User authenticatedUser,
            Integer postId,
            PostRequest request
    ) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));
        if(authenticatedUser == null) {
            throw new AccessDeniedException("Unauthorised");
        }
        if(!existingPost.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("Only the owner can update this post");
        }
        if(!existingPost.getVersion().equals(request.getVersion())) {
            throw new ConflictException("Post was modified by another user");
        }
        return mapAndSavePost(request, existingPost);
    }

    public void deletePostOrThrow(User authenticatedUser, Integer postId) throws AccessDeniedException {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));
        if(authenticatedUser == null) {
            throw new AccessDeniedException("Unauthorised");
        }
        if(!authenticatedUser.getRole().equals("ADMIN") &&
                !existingPost.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this post");
        }
        postRepository.delete(existingPost);
    }

    @Transactional
    public void likePost(User authenticatedUser, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));

        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, authenticatedUser.getId());

        if(alreadyLiked) throw new ConflictException("Post already Liked");

        PostLike like = new PostLike();
        like.setPost(post);
        like.setUser(authenticatedUser);

        postLikeRepository.save(like);

        postRepository.incrementLikes(postId);
    }

    @Transactional
    public void unlikePost(User authenticatedUser, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));

        boolean liked = postLikeRepository.existsByPostIdAndUserId(postId, authenticatedUser.getId());

        if(!liked) throw new ConflictException("Post not liked yet");

        postLikeRepository.deleteByPostIdAndUserId(postId, authenticatedUser.getId());

        postRepository.decrementLikes(postId);
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
        dto.setVersion(post.getVersion());
        dto.setLikes(post.getLikes());
        return dto;
    }


}
