package com.example.inssurify.domain;

import com.example.inssurify.domain.common.BaseEntity;
import com.example.inssurify.domain.enums.ContractCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractDocument extends BaseEntity {
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

    @OneToMany(mappedBy = "contractDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContractKeyword> keywordList = new ArrayList<>();
}

