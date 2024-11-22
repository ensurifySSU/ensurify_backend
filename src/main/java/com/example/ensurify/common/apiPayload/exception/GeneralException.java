package com.example.ensurify.common.apiPayload.exception;

import com.example.ensurify.common.apiPayload.code.BaseErrorCode;
import com.example.ensurify.common.apiPayload.code.ErrorReasonDTO;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public GeneralException(BaseErrorCode code){
        super(code.getReason().getCode() + ": " + code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }


}
