package com.myProject.demo.repository;

import com.myProject.demo.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
}
