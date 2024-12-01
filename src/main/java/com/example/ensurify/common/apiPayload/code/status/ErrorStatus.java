package com.example.ensurify.common.apiPayload.code.status;

import com.example.ensurify.common.apiPayload.code.BaseErrorCode;
import com.example.ensurify.common.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 일반적인 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _ENUM_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, "COMMON404", "일치하는 타입이 없습니다"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER400", "존재하지 않는 유저 정보입니다."),
    INVALID_PASSWORD(HttpStatus.CONFLICT, "USER401", "적절하지 않은 패스워드입니다."),
    USER_ACCESS_ONLY(HttpStatus.FORBIDDEN, "USER402", "USER(행원)만 사용 가능합니다."),
    GUEST_ACCESS_ONLY(HttpStatus.FORBIDDEN, "USER403", "GUEST(고객)만 사용 가능합니다"),

    // Client
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT400", "존재하지 않는 고객 정보입니다."),

    // Contract
    CONTRACT_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT400", "존재하지 않는 계약서 정보입니다."),
    MEETING_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT401", "존재하지 않는 회의실 정보입니다."),
    CHECK_NUM_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT402", "계약서 내에 존재하지 않는 체크 번호입니다."),
    USER_NOT_IN_ROOM(HttpStatus.FORBIDDEN, "CONTRACT403", "해당 ROOM에 입장할 수 없는 유저입니다."),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT404", "계약서 내에 존재하지 않는 페이지 번호입니다."),
    SIGN_NUM_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT405", "계약서 내에 존재하지 않는 서명란 번호입니다."),
    CONTRACT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CONTRACT406", "존재하지 않는 계약 내역입니다."),

    // JWT
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "JWT400", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT401", "만료된 토큰입니다."),

    // S3
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3400", "존재하지 않는 파일 정보입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
