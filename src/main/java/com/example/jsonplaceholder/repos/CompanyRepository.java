package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}