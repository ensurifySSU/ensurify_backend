package com.example.ensurify.repository;

import com.example.ensurify.domain.ContractDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractDocumentRepository extends JpaRepository<ContractDocument, Long> {
}
