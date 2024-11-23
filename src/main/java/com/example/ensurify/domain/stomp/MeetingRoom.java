package com.example.ensurify.domain.stomp;

import com.example.ensurify.domain.Clerk;
import com.example.ensurify.domain.ContractDocument;
import com.example.ensurify.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_document_id")
    private ContractDocument contractDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id")
    private Clerk clerk;
}
