package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Audit;
import com.example.jsonplaceholder.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface AuditsRepository extends JpaRepository<Audit, Long> {

}