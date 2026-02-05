package com.myProject.demo.repository;

import com.myProject.demo.entity.Post;
import com.myProject.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUser(User user);
}
