package com.example.inssurify.domain;

import com.example.inssurify.domain.common.BaseEntity;
import com.example.inssurify.domain.enums.ContractCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    private String name;

    private String pdfUrl;

    @Enumerated(EnumType.STRING)
    private ContractCategory category;
}

