package com.myProject.demo.repository;

import com.myProject.demo.entity.Post;
import com.myProject.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @EntityGraph(attributePaths = "user")
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByUser(
            User user,
            Pageable pageable
    );

    @Modifying
    @Query("""
    UPDATE Post p
    SET p.likes = p.likes + 1
    WHERE p.id = :postId
    """)
    int incrementLikes(Integer postId);

    @Modifying
    @Query("""
    UPDATE Post p
    SET p.likes = p.likes - 1
    WHERE p.id = :postId AND p.likes > 0
    """)
    int decrementLikes(Integer postId);
}
