package com.example.inssurify.domain.stomp.actions;

import com.example.inssurify.domain.common.BaseEntity;
import com.example.inssurify.domain.stomp.MeetingRoom;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`check`") // SQL 예약어와의 충돌 방지
public class Check extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private MeetingRoom room;
}
