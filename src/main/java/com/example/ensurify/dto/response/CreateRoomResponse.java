package com.example.ensurify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomResponse {

    @Schema(description = "회의실 id", nullable = false, example = "1")
    private Long roomId;
}
