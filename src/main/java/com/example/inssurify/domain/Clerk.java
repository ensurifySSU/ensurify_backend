package com.example.inssurify.domain;

import com.example.inssurify.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clerk extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "clerk", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contract> contractList = new ArrayList<>();

    private String name;

    private String imageUrl;

    private String username;

    private String password;
}

