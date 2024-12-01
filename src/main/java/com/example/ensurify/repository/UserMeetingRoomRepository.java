package com.example.ensurify.repository;

import com.example.ensurify.domain.UserMeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMeetingRoomRepository extends JpaRepository<UserMeetingRoom, Long> {
}
