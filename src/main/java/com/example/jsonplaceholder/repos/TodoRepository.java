package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserId(long id);

}