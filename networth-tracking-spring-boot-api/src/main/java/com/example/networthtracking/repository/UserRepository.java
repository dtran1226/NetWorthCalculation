package com.example.networthtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.networthtracking.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
