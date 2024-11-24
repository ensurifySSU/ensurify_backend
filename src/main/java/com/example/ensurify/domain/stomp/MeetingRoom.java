package com.example.ensurify.domain.stomp;

import com.example.ensurify.domain.ContractHistory;
import com.example.ensurify.domain.ContractKeyword;
import com.example.ensurify.domain.User;
import com.example.ensurify.domain.ContractDocument;
import com.example.ensurify.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_document_id")
    private ContractDocument contractDocument;

    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserMeetingRoom> userMeetingRooms = new ArrayList<>();
}
