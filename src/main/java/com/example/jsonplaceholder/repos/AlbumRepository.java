package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Address;
import com.example.jsonplaceholder.models.Album;
import com.example.jsonplaceholder.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByUserId(long id);

}