package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByAlbumId(long id);

}