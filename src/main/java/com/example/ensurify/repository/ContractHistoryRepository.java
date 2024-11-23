package com.example.ensurify.repository;

import com.example.ensurify.domain.ContractHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractHistoryRepository extends JpaRepository<ContractHistory, Long> {
}
