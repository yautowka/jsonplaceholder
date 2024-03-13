package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Post;
import com.example.jsonplaceholder.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(long id);

}