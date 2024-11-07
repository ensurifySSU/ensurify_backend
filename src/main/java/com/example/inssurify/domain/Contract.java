package com.example.inssurify.domain;

import com.example.inssurify.domain.common.BaseEntity;
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
    @JoinColumn(name = "contract_document_id")
    private ContractDocument contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id")
    private Clerk clerk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
