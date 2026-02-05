package com.myProject.demo.repository;

import com.myProject.demo.entity.Post;
import com.myProject.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByUser(
            User user,
            Pageable pageable
    );
}
