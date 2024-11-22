package com.example.ensurify.repository;

import com.example.ensurify.domain.Clerk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClerkRepository extends JpaRepository<Clerk, Long> {

    Optional<Clerk> findByUsername(String username);
}

