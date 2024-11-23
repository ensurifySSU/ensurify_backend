package com.example.ensurify.domain;

import com.example.ensurify.domain.common.BaseEntity;
import com.example.ensurify.domain.enums.ContractCategory;
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

    private String name;

    private int checkTotal;

    private int signTotal;

    private int pageNum;

    @Enumerated(EnumType.STRING)
    private ContractCategory category;

    @OneToMany(mappedBy = "contractDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContractKeyword> keywordList = new ArrayList<>();
}

