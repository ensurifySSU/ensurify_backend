package com.example.inssurify.repository;

import com.example.inssurify.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
