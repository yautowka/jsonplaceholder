package com.example.jsonplaceholder.repos;


import com.example.jsonplaceholder.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}