package com.example.ensurify.repository;

import com.example.ensurify.domain.stomp.actions.Check;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepository extends JpaRepository<Check, Long> {
}

