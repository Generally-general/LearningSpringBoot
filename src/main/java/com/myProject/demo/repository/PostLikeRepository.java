package com.myProject.demo.repository;

import com.myProject.demo.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);

    void deleteByPostIdAndUserId(Integer postId, Integer userId);
}
