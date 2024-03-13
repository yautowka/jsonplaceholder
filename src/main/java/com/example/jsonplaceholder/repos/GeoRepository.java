package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Geo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoRepository extends JpaRepository<Geo, Long> {

}