package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.UserFromServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFromServerRepository extends JpaRepository<UserFromServer, Long> {

}